import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.Produto;
import model.Venda;
import repository.ProdutoRepository;
import repository.ProdutoRepositoryImpl;
import repository.VendaRepository;
import repository.VendaRepositoryImpl;
import service.ProdutoService;
import service.VendaService;

public class Main {
    private static ProdutoService produtoService;
    private static VendaService vendaService;
    private static Scanner scanner;

    public static void main(String[] args) {
        ProdutoRepository produtoRepository = new ProdutoRepositoryImpl();
        VendaRepository vendaRepository = new VendaRepositoryImpl();
        produtoService = new ProdutoService(produtoRepository);
        vendaService = new VendaService(vendaRepository, produtoRepository);
        scanner = new Scanner(System.in);

        int opcao;
        do {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha após a leitura do inteiro

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    atualizarProduto();
                    break;
                case 3:
                    registrarVenda();
                    break;
                case 4:
                    emitirRelatorios();
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private static void exibirMenu() {
        System.out.println("----- MENU -----");
        System.out.println("1. Cadastrar produto");
        System.out.println("2. Atualizar produto");
        System.out.println("3. Registrar venda");
        System.out.println("4. Emitir relatórios");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarProduto() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Preço: ");
        double preco = scanner.nextDouble();
        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha após a leitura do inteiro

        Produto produto = new Produto(codigo, descricao, preco, quantidade);
        produtoService.cadastrar(produto);
    }

    private static void atualizarProduto() {
        System.out.print("Código do produto a ser atualizado: ");
        String codigo = scanner.nextLine();
        Produto produto = produtoService.buscarPorCodigo(codigo);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Nova descrição (deixe em branco para manter a atual): ");
        String descricao = scanner.nextLine();
        if (!descricao.isEmpty()) {
            produto.setDescricao(descricao);
        }

        System.out.print("Novo preço (digite 0 para manter o atual): ");
        double preco = scanner.nextDouble();
        if (preco != 0) {
            produto.setPreco(preco);
        }

        System.out.print("Nova quantidade (digite -1 para manter a atual): ");
        int quantidade = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha após a leitura do inteiro
        if (quantidade != -1) {
            produto.setQuantidade(quantidade);
        }

        produtoService.atualizar(produto);
    }

    private static void registrarVenda() {
        System.out.print("Data da venda (formato: dd/mm/aaaa): ");
        String dataString = scanner.nextLine();
        LocalDate data = LocalDate.parse(dataString, Venda.FORMATTER);

        System.out.print("Nome do cliente: ");
        String cliente = scanner.nextLine();

        List<Venda.Item> itens = new ArrayList<>();
        String continuar;
        do {
            System.out.print("Código do produto: ");
            String codigoProduto = scanner.nextLine();
            Produto produto = produtoService.buscarPorCodigo(codigoProduto);
            if (produto == null) {
                System.out.println("Produto não encontrado. A venda não será registrada.");
                return;
            }

            System.out.print("Quantidade: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha após a leitura do inteiro

            Venda.Item item = new Venda.Item(produto, quantidade);
            itens.add(item);

            System.out.print("Deseja adicionar mais um produto? (S/N): ");
            continuar = scanner.nextLine();
        } while (continuar.equalsIgnoreCase("S"));

        Venda venda = new Venda(data, cliente, itens);
        vendaService.registrar(venda);
    }

    private static void emitirRelatorios() {
        System.out.println("----- RELATÓRIOS -----");
        System.out.println("1. Inventário");
        System.out.println("2. Vendas do dia");
        System.out.println("3. Vendas geral");
        System.out.println("4. Itens mais e menos vendidos");
        System.out.println("5. Clientes que mais compram");
        System.out.print("Escolha uma opção: ");
        int opcaoRelatorio = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha após a leitura do inteiro

        switch (opcaoRelatorio) {
            case 1:
                emitirRelatorioInventario();
                break;
            case 2:
                emitirRelatorioVendasDoDia();
                break;
            case 3:
                emitirRelatorioVendasGeral();
                break;
            case 4:
                emitirRelatorioItensMaisEMenosVendidos();
                break;
            case 5:
                emitirRelatorioClientesQueMaisCompram();
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void emitirRelatorioInventario() {
        List<Produto> produtos = produtoService.listarTodos();
        System.out.println("----- INVENTÁRIO -----");
        for (Produto produto : produtos) {
            System.out.println("Código: " + produto.getCodigo());
            System.out.println("Descrição: " + produto.getDescricao());
            System.out.println("Preço: " + produto.getPreco());
            System.out.println("Quantidade: " + produto.getQuantidade());
            System.out.println("----------------------");
        }
    }

    private static void emitirRelatorioVendasDoDia() {
        System.out.print("Data das vendas (formato: dd/mm/aaaa): ");
        String dataString = scanner.nextLine();
        LocalDate data = LocalDate.parse(dataString, Venda.FORMATTER);

        List<Venda> vendas = vendaService.buscarPorData(data);
        System.out.println("----- VENDAS DO DIA " + data.format(Venda.FORMATTER) + " -----");
        for (Venda venda : vendas) {
            System.out.println("Cliente: " + venda.getCliente());
            System.out.println("Itens:");
            for (Venda.Item item : venda.getItens()) {
                System.out.println("- Produto: " + item.getProduto().getDescricao());
                System.out.println("  Quantidade: " + item.getQuantidade());
            }
            System.out.println("----------------------");
        }
    }

    private static void emitirRelatorioVendasGeral() {
        List<Venda> vendas = vendaService.listarTodas();
        System.out.println("----- VENDAS GERAL -----");
        for (Venda venda : vendas) {
            System.out.println("Data: " + venda.getData().format(Venda.FORMATTER));
            System.out.println("Cliente: " + venda.getCliente());
            System.out.println("Itens:");
            for (Venda.Item item : venda.getItens()) {
                System.out.println("- Produto: " + item.getProduto().getDescricao());
                System.out.println("  Quantidade: " + item.getQuantidade());
            }
            System.out.println("----------------------");
        }
    }

    private static void emitirRelatorioItensMaisEMenosVendidos() {
        List<Venda> vendas = vendaService.listarTodas();
        Map<String, Integer> quantidadeVendida = new HashMap<>();

        for (Venda venda : vendas) {
            for (Venda.Item item : venda.getItens()) {
                String codigoProduto = item.getProduto().getCodigo();
                int quantidade = item.getQuantidade();
                quantidadeVendida.put(codigoProduto, quantidadeVendida.getOrDefault(codigoProduto, 0) + quantidade);
            }
        }

        List<Map.Entry<String, Integer>> itensVendidos = new ArrayList<>(quantidadeVendida.entrySet());
        itensVendidos.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        System.out.println("----- ITENS MAIS VENDIDOS -----");
        for (int i = 0; i < Math.min(itensVendidos.size(), 5); i++) {
            Map.Entry<String, Integer> entry = itensVendidos.get(i);
            String codigoProduto = entry.getKey();
            int quantidade = entry.getValue();
            Produto produto = produtoService.buscarPorCodigo(codigoProduto);
            System.out.println("Produto: " + produto.getDescricao());
            System.out.println("Quantidade: " + quantidade);
            System.out.println("----------------------");
        }

        System.out.println("----- ITENS MENOS VENDIDOS -----");
        for (int i = itensVendidos.size() - 1; i >= Math.max(0, itensVendidos.size() - 5); i--) {
            Map.Entry<String, Integer> entry = itensVendidos.get(i);
            String codigoProduto = entry.getKey();
            int quantidade = entry.getValue();
            Produto produto = produtoService.buscarPorCodigo(codigoProduto);
            System.out.println("Produto: " + produto.getDescricao());
            System.out.println("Quantidade: " + quantidade);
            System.out.println("----------------------");
        }
    }

    private static void emitirRelatorioClientesQueMaisCompram() {
        List<Venda> vendas = vendaService.listarTodas();
        Map<String, Double> valorTotalPorCliente = new HashMap<>();

        for (Venda venda : vendas) {
            String cliente = venda.getCliente();
            double valorTotal = venda.calcularValorTotal();
            valorTotalPorCliente.put(cliente, valorTotalPorCliente.getOrDefault(cliente, 0.0) + valorTotal);
        }
    
        List<Map.Entry<String, Double>> clientesOrdenados = new ArrayList<>(valorTotalPorCliente.entrySet());
        clientesOrdenados.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    
        System.out.println("----- CLIENTES QUE MAIS COMPRAM -----");
        for (int i = 0; i < Math.min(clientesOrdenados.size(), 5); i++) {
            Map.Entry<String, Double> entry = clientesOrdenados.get(i);
            String cliente = entry.getKey();
            double valorTotal = entry.getValue();
            System.out.println("Cliente: " + cliente);
            System.out.println("Valor Total: " + valorTotal);
            System.out.println("----------------------");
        }
    }
    }