package br.ifsp.consulta_facil_api.ConsultaService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.ifsp.consulta_facil_api.dto.ConsultaDTO;
import br.ifsp.consulta_facil_api.dto.ProfissionalDTO;
import br.ifsp.consulta_facil_api.dto.PacienteDTO;
import br.ifsp.consulta_facil_api.model.Consulta;
import br.ifsp.consulta_facil_api.model.Horario;
import br.ifsp.consulta_facil_api.model.Profissional;
import br.ifsp.consulta_facil_api.repository.ConsultaRepository;
import br.ifsp.consulta_facil_api.repository.HorarioRepository;
import br.ifsp.consulta_facil_api.service.ConsultaService;

public class ConsultaServiceUnitTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ConsultaService consultaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mapeamento DTO -> Entidade
        when(modelMapper.map(any(ConsultaDTO.class), eq(Consulta.class))).thenAnswer(invocation -> {
            ConsultaDTO dto = invocation.getArgument(0);
            Consulta consulta = new Consulta();
            consulta.setHorario(dto.getHorario());
            if (dto.getProfissional() != null) {
                consulta.setProfissional(new Profissional(dto.getProfissional().getId()));
            }
            // mapear outros campos se precisar
            return consulta;
        });

        // Mapeamento Entidade -> DTO
        when(modelMapper.map(any(Consulta.class), eq(ConsultaDTO.class))).thenAnswer(invocation -> {
            Consulta consulta = invocation.getArgument(0);
            ConsultaDTO dto = new ConsultaDTO();
            dto.setHorario(consulta.getHorario());
            if (consulta.getProfissional() != null) {
                ProfissionalDTO profDto = new ProfissionalDTO();
                profDto.setId(consulta.getProfissional().getId());
                dto.setProfissional(profDto);
            }
            // mapear outros campos se precisar
            return dto;
        });
    }

    @Test
    void salvarConsulta_Sucesso() {
        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setHorario(LocalDateTime.now().plusDays(1));

        ProfissionalDTO profDto = new ProfissionalDTO();
        profDto.setId(1L);
        consultaDTO.setProfissional(profDto);

        PacienteDTO pacienteDto = new PacienteDTO();
        pacienteDto.setId(1L);
        consultaDTO.setPaciente(pacienteDto);

        when(consultaRepository.existsByProfissionalIdAndHorario(anyLong(), any())).thenReturn(false);

        when(horarioRepository.findByProfissionalIdAndDisponivelTrue(anyLong(), any(Pageable.class)))
            .thenAnswer(invocation -> {
                Pageable pageable = invocation.getArgument(1);

                var horarios = java.util.List.of(
                    new Horario(
                        null,
                        consultaDTO.getHorario(),
                        consultaDTO.getHorario().plusHours(1),
                        true,
                        new Profissional(profDto.getId())
                    )
                );

                return new PageImpl<>(horarios, pageable, horarios.size());
            });

        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsultaDTO resultado = consultaService.salvar(consultaDTO);

        assertNotNull(resultado);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void salvarConsulta_ComConflito_DeveLancar() {
        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setHorario(LocalDateTime.now().plusDays(1));
        ProfissionalDTO profDto = new ProfissionalDTO();
        profDto.setId(1L);
        consultaDTO.setProfissional(profDto);

        PacienteDTO pacienteDto = new PacienteDTO();
        pacienteDto.setId(1L);
        consultaDTO.setPaciente(pacienteDto);

        when(consultaRepository.existsByProfissionalIdAndHorario(anyLong(), any())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            consultaService.salvar(consultaDTO);
        });

        assertEquals("Já existe uma consulta agendada com este profissional neste horário.", ex.getMessage());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void salvarConsulta_HorarioIndisponivel_DeveLancar() {
        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setHorario(LocalDateTime.now().plusDays(1));
        ProfissionalDTO profDto = new ProfissionalDTO();
        profDto.setId(1L);
        consultaDTO.setProfissional(profDto);

        PacienteDTO pacienteDto = new PacienteDTO();
        pacienteDto.setId(1L);
        consultaDTO.setPaciente(pacienteDto);

        when(consultaRepository.existsByProfissionalIdAndHorario(anyLong(), any())).thenReturn(false);

        when(horarioRepository.findByProfissionalIdAndDisponivelTrue(anyLong(), any(Pageable.class)))
            .thenReturn(new PageImpl<>(java.util.Collections.emptyList()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            consultaService.salvar(consultaDTO);
        });

        assertEquals("O horário selecionado não está disponível para este profissional.", ex.getMessage());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deletarConsulta_Sucesso() {
        LocalDateTime horarioFuturo = LocalDateTime.now().plusDays(2);
        Consulta consulta = new Consulta();
        consulta.setId(1L);
        consulta.setHorario(horarioFuturo);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        assertDoesNotThrow(() -> {
            consultaService.deletar(1L);
        });

        verify(consultaRepository).deleteById(1L);
    }

    @Test
    void deletarConsulta_MenosDe24Horas_DeveLancar() {
        LocalDateTime horarioProximo = LocalDateTime.now().plusHours(23);
        Consulta consulta = new Consulta();
        consulta.setId(1L);
        consulta.setHorario(horarioProximo);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            consultaService.deletar(1L);
        });

        assertEquals("Cancelamentos só são permitidos com pelo menos 24h de antecedência.", ex.getMessage());
        verify(consultaRepository, never()).deleteById(anyLong());
    }

    @Test
    void deletarConsulta_NaoEncontrada_DeveLancar() {
        when(consultaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            consultaService.deletar(999L);
        });

        assertEquals("Consulta com ID 999 não encontrado", ex.getMessage());
        verify(consultaRepository, never()).deleteById(anyLong());
    }
}
