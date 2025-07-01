package br.ifsp.consulta_facil_api.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
    
    private final String codigo;
    private final String tipoRecurso;
    private final Long id;
    
    public RecursoNaoEncontradoException(String message) {
        super(message);
        this.codigo = "RECURSO_NAO_ENCONTRADO";
        this.tipoRecurso = null;
        this.id = null;
    }
    
    public RecursoNaoEncontradoException(String tipoRecurso, Long id) {
        super(String.format("%s com ID %d não encontrado", tipoRecurso, id));
        this.codigo = "RECURSO_NAO_ENCONTRADO";
        this.tipoRecurso = tipoRecurso;
        this.id = id;
    }
    
    public RecursoNaoEncontradoException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
        this.tipoRecurso = null;
        this.id = null;
    }
    
    public RecursoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
        this.codigo = "RECURSO_NAO_ENCONTRADO";
        this.tipoRecurso = null;
        this.id = null;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getTipoRecurso() {
        return tipoRecurso;
    }
    
    public Long getId() {
        return id;
    }
} 