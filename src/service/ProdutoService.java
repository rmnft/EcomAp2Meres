package service;

import java.util.List;

import model.Produto;
import repository.ProdutoRepository;

public class ProdutoService {
    private ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void cadastrar(Produto produto) {
        if (produtoRepository.buscarPorCodigo(produto.getCodigo()) != null) {
            System.out.println("Já existe um produto com o código " + produto.getCodigo());
            return;
        }
        produtoRepository.cadastrar(produto);
        System.out.println("Produto cadastrado com sucesso!");
    }

    public void atualizar(Produto produto) {
        if (produtoRepository.buscarPorCodigo(produto.getCodigo()) == null) {
            System.out.println("Não existe um produto com o código " + produto.getCodigo());
            return;
        }
        produtoRepository.atualizar(produto);
        System.out.println("Produto atualizado com sucesso!");
    }

    public Produto buscarPorCodigo(String codigo) {
        return produtoRepository.buscarPorCodigo(codigo);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.listarTodos();
    }
}