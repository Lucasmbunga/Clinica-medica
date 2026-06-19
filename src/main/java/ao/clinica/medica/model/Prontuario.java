package ao.clinica.medica.model;

import java.time.LocalDateTime;

public class Prontuario {
    private int id;
    private Consulta consulta;
    private String diagnostico;
    private String prescricao;
    private String observacoes;
    private LocalDateTime dataRegistro;

    public Prontuario() {}

    public Prontuario(int id, Consulta consulta, String diagnostico, String prescricao, String observacoes, LocalDateTime dataRegistro) {
        this.id = id;
        this.consulta = consulta;
        this.diagnostico = diagnostico;
        this.prescricao = prescricao;
        this.observacoes = observacoes;
        this.dataRegistro = dataRegistro;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getPrescricao() { return prescricao; }
    public void setPrescricao(String prescricao) { this.prescricao = prescricao; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
}
