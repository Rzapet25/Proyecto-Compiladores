document.getElementById('formulario').addEventListener('submit', function(e) {
    e.preventDefault(); // Esto detiene el envío del formulario

    fetch('/compilarWeb', {  // Cambiado a la URL correcta
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'codigo=' + encodeURIComponent(document.getElementById('codigo').value)
    })
    .then(response => response.text())
    .then(data => {
        // Como estamos usando Thymeleaf, necesitamos extraer solo el resultado
        // de la respuesta HTML completa
        const parser = new DOMParser();
        const htmlDoc = parser.parseFromString(data, 'text/html');
        const resultado = htmlDoc.querySelector('#resultado').textContent;
        document.getElementById('resultado').textContent = resultado;
    })
    .catch(error => {
        document.getElementById('resultado').textContent = 'Error: ' + error;
    });
});

// Función para validar si el campo de texto tiene contenido
function validarCampo() {
    const codigo = document.getElementById('codigo').value.trim();
    const botonCompilar = document.querySelector('button[type="submit"]');

    // Deshabilitar el botón si no hay texto
    botonCompilar.disabled = codigo === '';

    // Cambiar estilos del botón deshabilitado
    if (codigo === '') {
        botonCompilar.style.opacity = '0.5';
        botonCompilar.style.cursor = 'not-allowed';
    } else {
        botonCompilar.style.opacity = '1';
        botonCompilar.style.cursor = 'pointer';
    }
}

// Agregar estos estilos para el botón deshabilitado
document.head.insertAdjacentHTML('beforeend', `
        <style>
            button:disabled {
                background-color: #95a5a6 !important;
                transform: none !important;
            }
        </style>
        `);

// Inicializar la validación al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    const botonCompilar = document.querySelector('button[type="submit"]');

    // Deshabilitar el botón al inicio
    botonCompilar.disabled = true;
    botonCompilar.style.opacity = '0.5';
    botonCompilar.style.cursor = 'not-allowed';

    // Agregar evento para verificar mientras se escribe
    document.getElementById('codigo').addEventListener('input', validarCampo);
});

// Función para manejar la selección de archivo
document.getElementById('archivo').addEventListener('change', function() {
    const fileInput = document.getElementById('archivo');
    const fileNameDisplay = document.getElementById('nombre-archivo');
    const submitButton = document.querySelector('#formularioArchivo button');

    if (fileInput.files.length > 0) {
        fileNameDisplay.textContent = fileInput.files[0].name;
        submitButton.disabled = false;
        submitButton.style.opacity = '1';
        submitButton.style.cursor = 'pointer';
    } else {
        fileNameDisplay.textContent = 'Ningún archivo seleccionado';
        submitButton.disabled = true;
        submitButton.style.opacity = '0.5';
        submitButton.style.cursor = 'not-allowed';
    }
});

// Manejador para el selector de archivos
document.querySelector('.file-label').onclick = function(e) {
    e.preventDefault();
    document.getElementById('archivo').click();
};

document.getElementById('archivo').onclick = function(e) {
    e.stopPropagation();
};

// Manejador para el botón limpiar
document.getElementById('limpiar-codigo').addEventListener('click', function() {
    // Limpiar el área de código
    document.getElementById('codigo').value = '';
    // Limpiar el área de resultados
    document.getElementById('resultado').textContent = 'El Resultado del análisis aparecerá aquí...';
    // Actualizar el estado del botón Compilar
    validarCampo();
});