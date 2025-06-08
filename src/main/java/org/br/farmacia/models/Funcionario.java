package org.br.farmacia.models;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;
import org.br.farmacia.models.Beneficios;
import org.br.farmacia.models.Setor;

public class Funcionario {

    private String nome;
    private int id;
    private int idade;
    private Genero genero;
    private Cargo cargo;
    private double salario;
    private Beneficios beneficios;
    private Setor setor;

    public Funcionario(String nome, int id, int idade, Genero genero,
                       Cargo cargo, double salario) {

        if (idade < 0) throw new IllegalArgumentException("Idade inválida");
        if (salario <= 0) throw new IllegalArgumentException("Salário deve ser maior que zero");

        this.nome = nome;
        this.id = id;
        this.idade = idade;
        this.genero = genero;
        this.cargo = cargo;
        this.salario = salario;
        // benefícios e setor serão setados pelo Service
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public Beneficios getBeneficios() { return beneficios; }
    public void setBeneficios(Beneficios beneficios) { this.beneficios = beneficios; }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) { this.setor = setor; }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", id=" + id +
                ", idade=" + idade +
                ", genero=" + genero +
                ", cargo=" + cargo +
                ", setor=" + (setor != null ? setor.getTipoSetor() : "Nenhum") +
                ", salario=R$" + String.format("%.2f", salario) +
                ", beneficios=" + (beneficios != null ? beneficios.toString() : "Nenhum") +
                '}';
    }
}
