package org.br.farmacia.models;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class Funcionario {

    private String nome;
    private int id;
    private Date dataNascimento;
    private Genero genero;
    private Cargo cargo;
    private double salario;
    private Setor setor;
    private double valeRefeicao;
    private double valeAlimentacao;
    private double planoSaude;
    private double planoOdonto;

    public Funcionario(String nome,int id , Date dataNascimento , Genero genero,
                       Cargo cargo, double salario) {

        if (dataNascimento == null) throw new IllegalArgumentException("Idade inválida");
        if (salario <= 0) throw new IllegalArgumentException("Salário deve ser maior que zero");

        this.nome = nome;
        this.id = id;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.cargo = cargo;
        this.salario = salario;
        this.valeRefeicao = 0.0;
        this.valeAlimentacao = 0.0;
        this.planoSaude = 0.0;
        this.planoOdonto = 0.0;
    }

    public Funcionario(String nome, int id, Date dataNascimento, Genero genero,
                       Cargo cargo, double salario, double valeRefeicao,
                       double valeAlimentacao, double planoSaude, double planoOdonto) {
        this(nome, id, dataNascimento, genero, cargo, salario);
        this.valeRefeicao = valeRefeicao;
        this.valeAlimentacao = valeAlimentacao;
        this.planoSaude = planoSaude;
        this.planoOdonto = planoOdonto;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setIdade(Date dataNascimento) { this.dataNascimento = dataNascimento; }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public int getIdade() {
        LocalDate nascimento = dataNascimento.toLocalDate();
        return Period.between(nascimento, LocalDate.now()).getYears();
    }


    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public Setor getSetor() { return setor; }
    public void setSetor(Setor setor) { this.setor = setor; }

    public double getValeRefeicao() {
        return valeRefeicao;
    }

    public void setValeRefeicao(double valeRefeicao) {
        this.valeRefeicao = valeRefeicao;
    }

    public double getValeAlimentacao() {
        return valeAlimentacao;
    }

    public void setValeAlimentacao(double valeAlimentacao) {
        this.valeAlimentacao = valeAlimentacao;
    }

    public double getPlanoSaude() {
        return planoSaude;
    }

    public void setPlanoSaude(double planoSaude) {
        this.planoSaude = planoSaude;
    }

    public double getPlanoOdonto() {
        return planoOdonto;
    }

    public void setPlanoOdonto(double planoOdonto) {
        this.planoOdonto = planoOdonto;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", id=" + id +
                ", idade=" + dataNascimento +
                ", genero=" + genero +
                ", cargo=" + cargo +
                ", setor=" + (setor != null ? setor.getTipoSetor() : "Nenhum") +
                ", salario=R$" + String.format("%.2f", salario) +
                ", valeRefeicao=R$" + String.format("%.2f", valeRefeicao) +
                ", valeAlimentacao=R$" + String.format("%.2f", valeAlimentacao) +
                ", planoSaude=R$" + String.format("%.2f", planoSaude) +
                ", planoOdonto=R$" + String.format("%.2f", planoOdonto) +
                '}';
    }
}