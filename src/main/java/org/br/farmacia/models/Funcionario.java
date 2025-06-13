package org.br.farmacia.models;

import org.br.farmacia.enums.Cargo;
import org.br.farmacia.enums.Genero;

import java.time.LocalDate;

public class Funcionario {

    private int id;
    private String nome;
    private LocalDate dataNascimento;
    private Genero genero;
    private Cargo cargo;
    private double salario;
    private double valeRefeicao;
    private double valeAlimentacao;
    private double planoSaude;
    private double planoOdonto;
    private double percentualIrrf;
    private double percentualInss;
    private double bonificacao;
    private int setorId;
    private Setor setor = new Setor();

    public Funcionario() {}

    public Funcionario(int id, String nome, LocalDate dataNascimento, Genero genero, double salario, int setorId, Cargo cargo) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.salario = salario;
        this.setorId = setorId;
        this.cargo = cargo;
    }

    public Funcionario(int id, String nome, LocalDate dataNascimento, Genero genero, double salario, int setorId, Cargo cargo,double valeRefeicao, double valeAlimentacao, double planoSaude
            , double planoOdonto, double percentualIrrf, double percentualInss, double bonificacao) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
        this.salario = salario;
        this.setorId = setorId;
        this.cargo = cargo;
        this.valeRefeicao = valeRefeicao;
        this.valeAlimentacao = valeAlimentacao;
        this.planoSaude = planoSaude;
        this.planoOdonto = planoOdonto;
        this.percentualIrrf = percentualIrrf;
        this.percentualInss = percentualInss;
        this.bonificacao = bonificacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

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


    public void setPercentualIrrf(double percentualIrrf) {
        this.percentualIrrf = percentualIrrf;
    }

    public void setPercentualInss(double percentualInss) {
        this.percentualInss = percentualInss;
    }

    public double getBonificacao() {
        return bonificacao;
    }

    public void setBonificacao(double bonificacao) {
        this.bonificacao = bonificacao;
    }

    public int getSetorId() {
        return setorId;
    }

    public void setSetorId(int setorId) {
        this.setorId = setorId;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public double getSalarioLiquido() {
        double descontoIrrf = salario * (percentualIrrf / 100.0);
        double descontoInss = salario * (percentualInss / 100.0);

        return salario
                - descontoIrrf
                - descontoInss
                + valeRefeicao
                + valeAlimentacao
                + bonificacao
                - planoSaude
                - planoOdonto;
    }

    public double getPercentualIRRF() {
        return percentualIrrf;
    }

    public double getPercentualINSS() {
        return percentualInss;
    }
}
