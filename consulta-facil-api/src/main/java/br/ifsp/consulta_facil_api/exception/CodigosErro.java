package br.ifsp.consulta_facil_api.exception;

public final class CodigosErro {
    
    // Códigos de erro para Consultas
    public static final String CONFLITO_HORARIO = "CONFLITO_HORARIO";
    public static final String CANCELAMENTO_SEM_ANTECEDENCIA = "CANCELAMENTO_SEM_ANTECEDENCIA";
    public static final String CONSULTA_NAO_ENCONTRADA = "CONSULTA_NAO_ENCONTRADA";
    
    // Códigos de erro para Horários
    public static final String HORARIO_INDISPONIVEL = "HORARIO_INDISPONIVEL";
    public static final String HORARIO_INVALIDO = "HORARIO_INVALIDO";
    public static final String HORARIO_PASSADO = "HORARIO_PASSADO";
    
    // Códigos de erro para Usuários
    public static final String USUARIO_NAO_ENCONTRADO = "USUARIO_NAO_ENCONTRADO";
    public static final String EMAIL_JA_CADASTRADO = "EMAIL_JA_CADASTRADO";
    public static final String SENHA_INVALIDA = "SENHA_INVALIDA";
    public static final String PERMISSAO_INSUFICIENTE = "PERMISSAO_INSUFICIENTE";
    
    // Códigos de erro para Recursos
    public static final String RECURSO_NAO_ENCONTRADO = "RECURSO_NAO_ENCONTRADO";
    public static final String PACIENTE_NAO_ENCONTRADO = "PACIENTE_NAO_ENCONTRADO";
    public static final String PROFISSIONAL_NAO_ENCONTRADO = "PROFISSIONAL_NAO_ENCONTRADO";
    
    // Códigos de erro genéricos
    public static final String ERRO_VALIDACAO = "ERRO_VALIDACAO";
    public static final String ERRO_INTERNO = "ERRO_INTERNO";
    public static final String ACESSO_NEGADO = "ACESSO_NEGADO";
    
    private CodigosErro() {
        // Construtor privado para evitar instanciação
    }
} 