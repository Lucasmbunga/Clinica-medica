package ao.clinica.medica.model;
public class Endereco {
    private int id;
    private int idPaciente;
    private String rua;
    private String bairro;
    private String zona;
    private String numeroCasa;

    public Endereco() {}

    public Endereco(int id, int idPaciente, String rua, String bairro, String zona, String numeroCasa) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.rua = rua;
        this.bairro = bairro;
        this.zona = zona;
        this.numeroCasa = numeroCasa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getNumeroCasa() { return numeroCasa; }
    public void setNumeroCasa(String numeroCasa) { this.numeroCasa = numeroCasa; }
}