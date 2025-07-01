package br.ifsp.consulta_facil_api.exception;

public class HorarioException extends RuntimeException {
    
    private final String codigo;
    
    public HorarioException(String message) {
        super(message);
        this.codigo = "HORARIO_ERROR";
    }
    
    public HorarioException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }
    
    public HorarioException(String message, Throwable cause) {
        super(message, cause);
        this.codigo = "HORARIO_ERROR";
    }
    
    public HorarioException(String message, String codigo, Throwable cause) {
        super(message, cause);
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
} 