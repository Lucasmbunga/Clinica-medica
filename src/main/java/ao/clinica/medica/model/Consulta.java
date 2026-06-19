package ao.clinica.medica.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {
    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDate data;
    private LocalTime hora;
    private StatusConsulta status;

    public Consulta() {}

    public Consulta(int id, Paciente paciente, Medico medico, LocalDate data, LocalTime hora, StatusConsulta status) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.hora = hora;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }
}
