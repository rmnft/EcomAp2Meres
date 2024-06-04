package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Produto;

public class ProdutoRepositoryImpl implements ProdutoRepository {
    private static final String ARQUIVO_CADASTRO = "cadastro.txt";

    @Override
    public void cadastrar(Produto produto) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CADASTRO, true))) {
            writer.write(produto.getCodigo() + ";" + produto.getDescricao() + ";" + produto.getPreco() + ";" + produto.getQuantidade());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Produto produto) {
        List<Produto> produtos = listarTodos();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CADASTRO))) {
            for (Produto p : produtos) {
                if (p.getCodigo().equals(produto.getCodigo())) {
                    writer.write(produto.getCodigo() + ";" + produto.getDescricao() + ";" + produto.getPreco() + ";" + produto.getQuantidade());
                } else {
                    writer.write(p.getCodigo() + ";" + p.getDescricao() + ";" + p.getPreco() + ";" + p.getQuantidade());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    @Override
    public Produto buscarPorCodigo(String codigo) {
        List<Produto> produtos = listarTodos();
        for (Produto produto : produtos) {
            if (produto.getCodigo().equals(codigo)) {
                return produto;
            }
        }
        return null;
    }

    @Override
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CADASTRO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                Produto produto = new Produto(dados[0], dados[1], Double.parseDouble(dados[2]), Integer.parseInt(dados[3]));
                produtos.add(produto);
            }
        } catch (IOException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }
}