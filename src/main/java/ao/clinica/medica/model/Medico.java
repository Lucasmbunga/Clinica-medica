package ao.clinica.medica.model;

public class Medico {
    private int id;
    private Usuario usuario; // Relacionamento com Usuário para Controle de Acesso
    private String crm;
    private String especialidade;
    private String telefone;

    public Medico() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}