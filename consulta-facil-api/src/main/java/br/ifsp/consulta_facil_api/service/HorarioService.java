package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.HorarioDTO;
import br.ifsp.consulta_facil_api.model.Horario;
import br.ifsp.consulta_facil_api.repository.HorarioRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HorarioService {

	 @Autowired
	    private HorarioRepository horarioRepository;

	    @Autowired
	    private ModelMapper modelMapper;

	    public Page<HorarioDTO> listar(Pageable pageable) {
	        Page<Horario> horarios = horarioRepository.findAll(pageable);
	        return horarios.map(horario -> modelMapper.map(horario, HorarioDTO.class));
	    }

	    public Optional<HorarioDTO> buscarPorId(Long id) {
	        Optional<Horario> horario = horarioRepository.findById(id);
	        return horario.map(h -> modelMapper.map(h, HorarioDTO.class));
	    }

	    public HorarioDTO salvar(HorarioDTO dto) {
	        Horario horario = modelMapper.map(dto, Horario.class);
	        horario = horarioRepository.save(horario);
	        return modelMapper.map(horario, HorarioDTO.class);
	    }

	    public void deletar(Long id) {
	        horarioRepository.deleteById(id);
	    }
}
