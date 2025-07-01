package br.ifsp.consulta_facil_api.exception;

public class ConsultaException extends RuntimeException {
    
    private final String codigo;
    
    public ConsultaException(String message) {
        super(message);
        this.codigo = "CONSULTA_ERROR";
    }
    
    public ConsultaException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }
    
    public ConsultaException(String message, Throwable cause) {
        super(message, cause);
        this.codigo = "CONSULTA_ERROR";
    }
    
    public ConsultaException(String message, String codigo, Throwable cause) {
        super(message, cause);
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
} 