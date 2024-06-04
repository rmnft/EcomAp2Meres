package service;

import java.time.LocalDate;
import java.util.List;

import model.Produto;
import model.Venda;
import repository.ProdutoRepository;
import repository.VendaRepository;

public class VendaService {
    private VendaRepository vendaRepository;
    private ProdutoRepository produtoRepository;

    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
    }

    public void registrar(Venda venda) {
        for (Venda.Item item : venda.getItens()) {
            Produto produto = produtoRepository.buscarPorCodigo(item.getProduto().getCodigo());
            if (produto == null) {
                System.out.println("Produto com código " + item.getProduto().getCodigo() + " não encontrado.");
                return;
            }
            if (produto.getQuantidade() < item.getQuantidade()) {
                System.out.println("Quantidade insuficiente em estoque para o produto " + produto.getCodigo());
                System.out.println("Quantidade disponível: " + produto.getQuantidade());
                return;
            }
            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            produtoRepository.atualizar(produto);
        }
        vendaRepository.registrar(venda);
        System.out.println("Venda registrada com sucesso!");
    }

    public List<Venda> buscarPorData(LocalDate data) {
        return vendaRepository.buscarPorData(data);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.listarTodas();
    }
}