package br.ifsp.consulta_facil_api.service;

import br.ifsp.consulta_facil_api.dto.ConsultaDTO;
import br.ifsp.consulta_facil_api.model.Consulta;
import br.ifsp.consulta_facil_api.repository.ConsultaRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsultaService {

	@Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<ConsultaDTO> listar(Pageable pageable) {
        Page<Consulta> consultas = consultaRepository.findAll(pageable);
        return consultas.map(consulta -> modelMapper.map(consulta, ConsultaDTO.class));
    }

    public Optional<ConsultaDTO> buscarPorId(Long id) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        return consulta.map(c -> modelMapper.map(c, ConsultaDTO.class));
    }

    public ConsultaDTO salvar(ConsultaDTO dto) {
        Consulta consulta = modelMapper.map(dto, Consulta.class);
        consulta = consultaRepository.save(consulta);
        return modelMapper.map(consulta, ConsultaDTO.class);
    }

    public void deletar(Long id) {
        consultaRepository.deleteById(id);
    }
}