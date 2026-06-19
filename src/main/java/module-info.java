module ao.clinica.medica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Permite que o JavaFX aceda aos seus controllers e modelos via reflexão
    opens ao.clinica.medica.controller to javafx.fxml;
    opens ao.clinica.medica.model to javafx.base;

    // Exporta os pacotes para que fiquem visíveis ao ecossistema do JavaFX
    exports ao.clinica.medica;
    exports ao.clinica.medica.controller;
    exports ao.clinica.medica.dao;
    exports ao.clinica.medica.model;
}