package org.br.farmacia.services.security;


import org.br.farmacia.enums.Cargo;
import java.util.HashMap;
import java.util.Map;

public class CargoComPermissao {

    public static class verificaPermissoes{  // POJO (classe auxiliar) para permissões detalhadas dos enums
        public final boolean podeGerenciarFuncionarios;
        public final boolean podeAlterarEgerenciarProdutos;
        public final boolean podeAlterarSalario;
        public final boolean podeAcessarCaixaTotal;
        public final boolean gerenciarNegociosEmAndamento;
        public final boolean gerenciarTransportadora;

        public verificaPermissoes( boolean podeGerenciarFuncionarios, boolean podeAlterarEgerenciarProdutos, boolean podeAlterarSalario, boolean podeAcessarCaixaTotal, boolean gerenciarNegociosEmAndamento, boolean gerenciarTransportadora){
            this.podeGerenciarFuncionarios = podeGerenciarFuncionarios;
            this.podeAlterarEgerenciarProdutos = podeAlterarEgerenciarProdutos;
            this.podeAlterarSalario = podeAlterarSalario;
            this.podeAcessarCaixaTotal = podeAcessarCaixaTotal;
            this.gerenciarNegociosEmAndamento  = gerenciarNegociosEmAndamento;
            this.gerenciarTransportadora = gerenciarTransportadora;
        }


    }

    private static final Map<Cargo,verificaPermissoes> PERMISSOES_CARGO = new HashMap();
    static {
        PERMISSOES_CARGO.put(Cargo.GERENTE, new verificaPermissoes(true,true,false,true,true,true));
        PERMISSOES_CARGO.put(Cargo.RH, new verificaPermissoes(true,false,true,false,false,true));
        PERMISSOES_CARGO.put(Cargo.FINANCEIRO, new verificaPermissoes(true,false,false,true,false,true));
    }

    public static verificaPermissoes getPermissao(Cargo cargo){
        return PERMISSOES_CARGO.get(cargo);
    }
}
