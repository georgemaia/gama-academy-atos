package net.atos.api.notafiscal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;
import javax.ws.rs.NotFoundException;

import org.springframework.stereotype.Service;

import net.atos.api.notafiscal.domain.NotaFiscalVO;
import net.atos.api.notafiscal.factory.NotaFiscalFactory;
import net.atos.api.notafiscal.repository.NotaFiscalRepository;
import net.atos.api.notafiscal.repository.entity.NotaFiscalEntity;

@Service
public class BuscaNotaFiscalService {	
	
	private Validator validator;
	
	private NotaFiscalRepository notaFiscalRepositoy;
	
	public BuscaNotaFiscalService(Validator v, NotaFiscalRepository repository) {
		this.validator = v;		
		this.notaFiscalRepositoy = repository; 	
	}


	public List<NotaFiscalVO> porDocumento(String documento) {
		List<NotaFiscalEntity> notasFiscaisEntities = notaFiscalRepositoy.findByDocumento(documento)
				.orElseThrow(()->
				     new NotFoundException("Nenhuma nota fiscal para o documento informado"));
		
		return notasFiscaisEntities.stream()
				.map(NotaFiscalFactory::new)
				.map(NotaFiscalFactory::toVO)
				.collect(Collectors.toList()); 
				
	}


	public List<NotaFiscalVO>  porPeriodoDataEmissao(LocalDate dataInicio, LocalDate dataFim) {
		List<NotaFiscalEntity> notasFiscaisEntities = notaFiscalRepositoy.findByDataEmissaoBetween(dataInicio,dataFim)
				.orElseThrow(()->
				     new NotFoundException("Nenhuma nota fiscal para o periodo informado"));
		
		return notasFiscaisEntities.stream()
				.map(NotaFiscalFactory::new)
				.map(NotaFiscalFactory::toVO)
				.collect(Collectors.toList()); 
		
	}

	public NotaFiscalVO porId(long id) {
		NotaFiscalEntity notaFiscalEntity = this.notaFiscalRepositoy.findById(id)
				.orElseThrow(()-> new NotFoundException("Não encontrada a nf com id = "+id));
		
		return new NotaFiscalFactory(notaFiscalEntity).toVO();
	}

		
	
}