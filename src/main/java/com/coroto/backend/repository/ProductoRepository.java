package com.coroto.backend.repository;

import com.coroto.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    //AQUI VAAAAAA CUALQIER MÉTODO EXTRA QUE YO NECESITE
    //PARA MANIPULAR LOS DATOS
    //miMetodoPropio();
}
