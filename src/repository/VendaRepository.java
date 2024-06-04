package repository;

import java.time.LocalDate;
import java.util.List;

import model.Venda;

public interface VendaRepository {
    void registrar(Venda venda);
    List<Venda> buscarPorData(LocalDate data);
    List<Venda> listarTodas();
}