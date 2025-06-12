package org.br.farmacia.services;

import org.br.farmacia.enums.TipoTransacao;
import org.br.farmacia.models.Caixa;
import org.br.farmacia.models.Empresa;
import org.br.farmacia.models.Funcionario;
import org.br.farmacia.repositories.CaixaRepository;
import org.br.farmacia.repositories.EmpresaRepository;
import org.br.farmacia.repositories.FuncionarioRepository;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.util.List;

public class CaixaServices {

    private final CaixaRepository caixaRepository;
    private final EmpresaRepository empresaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public CaixaServices(ServletContext context) {
        this.caixaRepository = new CaixaRepository(context);
        this.empresaRepository = new EmpresaRepository(context);
        this.funcionarioRepository = new FuncionarioRepository(context);
    }

    public Caixa registrarTransacao(TipoTransacao tipo, double valor, String descricao, int empresaId) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser positivo.");
        }

        Empresa empresa = empresaRepository.findById(empresaId);
        if (empresa == null) {
            throw new IllegalArgumentException("Empresa não encontrada com ID: " + empresaId);
        }

        Caixa novaTransacao = new Caixa(tipo, valor, LocalDateTime.now(), descricao, empresaId);
        boolean sucesso = caixaRepository.save(novaTransacao);

        if (sucesso) {
            if (tipo == TipoTransacao.ENTRADA) {
                empresa.setCaixaTotal(empresa.getCaixaTotal() + valor);
            } else {
                if (empresa.getCaixaTotal() < valor) {
                    System.err.println("Atenção: Saldo insuficiente para realizar a saída de caixa. Saldo atual: " + empresa.getCaixaTotal() + ", Valor da saída: " + valor);
                }
                empresa.setCaixaTotal(empresa.getCaixaTotal() - valor);
            }
            empresaRepository.update(empresa);
            return novaTransacao;
        }
        return null;
    }

    public double getCaixaTotal(int empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId);
        if (empresa != null) {
            return empresa.getCaixaTotal();
        }
        return 0.0;
    }

    public List<Caixa> listarTransacoes(int empresaId) {
        return caixaRepository.findByEmpresaId(empresaId);
    }


    public void inicializarCaixaEmpresa(int empresaId, double valorInicial) {
        Empresa empresa = empresaRepository.findById(empresaId);
        if (empresa == null) {
            throw new IllegalArgumentException("Empresa não encontrada para inicializar o caixa.");
        }
        empresa.setCaixaTotal(valorInicial);
        empresaRepository.update(empresa);
    }


    public Caixa registrarPagamentoSalarios(int empresaId) {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        double totalSalarios = 0;
        for (Funcionario f : funcionarios) {
            totalSalarios += f.getSalarioLiquido();
            totalSalarios += f.getValeRefeicao();
            totalSalarios += f.getValeAlimentacao();
            totalSalarios += f.getPlanoSaude();
            totalSalarios += f.getPlanoOdonto();
        }

        if (totalSalarios > 0) {
            return registrarTransacao(TipoTransacao.SAIDA, totalSalarios, "Pagamento de salários e benefícios", empresaId);
        }
        return null;
    }

    public Caixa registrarCompraDeProdutos(int empresaId, double valorCompra, String descricao) {
        if (valorCompra <= 0) {
            throw new IllegalArgumentException("O valor da compra deve ser positivo.");
        }
        return registrarTransacao(TipoTransacao.SAIDA, valorCompra, descricao, empresaId);
    }

    public Caixa registrarVendaDeProdutos(int empresaId, double valorVenda, String descricao) {
        if (valorVenda <= 0) {
            throw new IllegalArgumentException("O valor da venda deve ser positivo.");
        }
        return registrarTransacao(TipoTransacao.ENTRADA, valorVenda, descricao, empresaId);
    }

    public double calcularEstimativaLucroMensal(int empresaId) {
        return 0.0;
    }

    public double calcularEstimativaLucroAnual(int empresaId) {
        return 0.0;
    }
}