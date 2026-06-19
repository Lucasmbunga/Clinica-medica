package ao.clinica.medica.model;

public class Telefone {
    private int id;
    private int idPaciente;
    private int idMedico;
    private String numero;

    public Telefone() {}

    public Telefone(int id, int idPaciente, int idMedico, String numero) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.numero = numero;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}