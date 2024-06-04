package repository;

import java.util.List;

import model.Produto;

public interface ProdutoRepository {
    void cadastrar(Produto produto);
    void atualizar(Produto produto);
    Produto buscarPorCodigo(String codigo);
    List<Produto> listarTodos();
}