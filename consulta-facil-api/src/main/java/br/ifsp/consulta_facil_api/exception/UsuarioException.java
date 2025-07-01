package br.ifsp.consulta_facil_api.exception;

public class UsuarioException extends RuntimeException {
    
    private final String codigo;
    
    public UsuarioException(String message) {
        super(message);
        this.codigo = "USUARIO_ERROR";
    }
    
    public UsuarioException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }
    
    public UsuarioException(String message, Throwable cause) {
        super(message, cause);
        this.codigo = "USUARIO_ERROR";
    }
    
    public UsuarioException(String message, String codigo, Throwable cause) {
        super(message, cause);
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
} 