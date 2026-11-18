package com.pruebas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.pruebas.model.Inscripcion;
import com.pruebas.model.services.InscripcionService;
import com.pruebas.repository.InscripcionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InscripcionServiceTest {
    //DATOS DEL ALUMNO
    private static final String ALUMNO = "Andre Lopez";
    private static final String CODIGO = "I202500637";

    @Mock
    private InscripcionRepository repository;

    @InjectMocks
    private InscripcionService service;

    @BeforeAll
    static void identificarme(){
        System.out.println("Alumno: " + ALUMNO + " Código: " + CODIGO);
    }

    //metodo para no repetir lo mismo en cada caso
    private Inscripcion crearInscripcion(String codigoEstudiante, Long cursoId){
        Inscripcion i = new Inscripcion();
        i.setCodigoEstudiante(codigoEstudiante);
        i.setCursoId(cursoId);
        return i;
    }

    //CASO 1
    @Test
    @DisplayName("CASO 1: inscribir a un estudiante correctamente")
    void inscribirEstudianteCorrectamente(){
        //Arrange
        Inscripcion nueva = crearInscripcion("I2024050", 2L);
        when(repository.existsByCodigoEstudianteAndCursoId("I2024050", 2L)).thenReturn(false);
        when(repository.save(nueva)).thenReturn(nueva);

        //Act
        Inscripcion resultado = service.inscribir(nueva);

        //Assert
        assertNotNull(resultado);
        assertEquals("I2024050", resultado.getCodigoEstudiante());
        assertEquals(2L, resultado.getCursoId());
        verify(repository, times(1)).save(nueva);
    }

    //CASO 2
    @Test
    @DisplayName("CASO 2: intentar inscribir a un estudiante que ya esta inscrito")
    void inscribirEstudianteDuplicadoLanzaExcepcion(){
        //Arrange
        Inscripcion repetida = crearInscripcion("I2024001", 1L);
        when(repository.existsByCodigoEstudianteAndCursoId("I2024001", 1L)).thenReturn(true);

        //Act
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.inscribir(repetida));

        //Assert
        assertEquals("Estudiante ya inscrito", ex.getMessage());
        verify(repository, never()).save(any(Inscripcion.class));
    }

    //CASO 3
    @Test
    @DisplayName("CASO 3: listar las inscripciones de un estudiante")
    void listarInscripcionesDeEstudiante(){
        //Arrange
        List<Inscripcion> lista = new ArrayList<>();
        lista.add(crearInscripcion("I2024001", 1L));
        lista.add(crearInscripcion("I2024001", 3L));
        when(repository.findByCodigoEstudiante("I2024001")).thenReturn(lista);

        //Act
        List<Inscripcion> resultado = service.listarPorEstudiante("I2024001");

        //Assert
        assertEquals(2, resultado.size());
        for (Inscripcion i : resultado) {
            assertEquals("I2024001", i.getCodigoEstudiante());
        }
    }
}
