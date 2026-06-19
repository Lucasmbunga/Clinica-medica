package ao.clinica.medica.model;
import java.time.LocalDate;

public class Paciente {
    private int id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento; // Uso da API moderna do Java 8+ (LocalDate)
    private String telefone;
   // private String historicoMedico;

    public Paciente() {}

    public Paciente(int id, String nome, String cpf, LocalDate dataNascimento, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        //this.historicoMedico = historicoMedico;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    //public String getHistoricoMedico() { return historicoMedico; }
    //public void setHistoricoMedico(String historicoMedico) { this.historicoMedico = historicoMedico; }
}