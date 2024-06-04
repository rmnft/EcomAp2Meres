package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.Produto;
import model.Venda;

public class VendaRepositoryImpl implements VendaRepository {
    private static final String ARQUIVO_VENDAS = "vendas.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void registrar(Venda venda) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_VENDAS, true))) {
            writer.write(venda.getData().format(FORMATTER) + ";" + venda.getCliente());
            for (Venda.Item item : venda.getItens()) {
                writer.write(";" + item.getProduto().getCodigo() + "," + item.getQuantidade());
            }
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao registrar venda: " + e.getMessage());
        }
    }

    @Override
    public List<Venda> buscarPorData(LocalDate data) {
        List<Venda> vendas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_VENDAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                LocalDate dataVenda = LocalDate.parse(dados[0], FORMATTER);
                if (dataVenda.equals(data)) {
                    String cliente = dados[1];
                    List<Venda.Item> itens = new ArrayList<>();
                    for (int i = 2; i < dados.length; i++) {
                        String[] itemDados = dados[i].split(",");
                        String codigoProduto = itemDados[0];
                        int quantidade = Integer.parseInt(itemDados[1]);
                        Produto produto = buscarProdutoPorCodigo(codigoProduto);
                        if (produto != null) {
                            Venda.Item item = new Venda.Item(produto, quantidade);
                            itens.add(item);
                        }
                    }
                    Venda venda = new Venda(dataVenda, cliente, itens);
                    vendas.add(venda);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao buscar vendas por data: " + e.getMessage());
        }
        return vendas;
    }

    @Override
    public List<Venda> listarTodas() {
        List<Venda> vendas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_VENDAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                LocalDate dataVenda = LocalDate.parse(dados[0], FORMATTER);
                String cliente = dados[1];
                List<Venda.Item> itens = new ArrayList<>();
                for (int i = 2; i < dados.length; i++) {
                    String[] itemDados = dados[i].split(",");
                    String codigoProduto = itemDados[0];
                    int quantidade = Integer.parseInt(itemDados[1]);
                    Produto produto = buscarProdutoPorCodigo(codigoProduto);
                    if (produto != null) {
                        Venda.Item item = new Venda.Item(produto, quantidade);
                        itens.add(item);
                    }
                }
                Venda venda = new Venda(dataVenda, cliente, itens);
                vendas.add(venda);
            }
        } catch (IOException e) {
            System.out.println("Erro ao listar todas as vendas: " + e.getMessage());
        }
        return vendas;
    }

    private Produto buscarProdutoPorCodigo(String codigo) {
        ProdutoRepository produtoRepository = new ProdutoRepositoryImpl();
        return produtoRepository.buscarPorCodigo(codigo);
    }
}