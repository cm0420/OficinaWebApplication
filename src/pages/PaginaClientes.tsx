// src/pages/PaginaClientes.tsx

import { useState, useEffect } from 'react';
import { Cliente } from '../models/cliente'; //
import { getClientes } from '../services/clienteService'; //

const PaginaClientes = () => {
    const [clientes, setClientes] = useState<Cliente[]>([]);

    // Este hook do React executa o código aqui dentro quando a página carrega
    useEffect(() => {
        const fetchClientes = async () => {
            try {
                console.log("Buscando clientes...");
                const data = await getClientes(); // Usa o serviço para buscar os dados
                console.log("Clientes recebidos:", data);
                setClientes(data); // Guarda os dados recebidos para serem mostrados na tela
            } catch (error) {
                console.error("Erro ao buscar clientes:", error);
            }
        };

        fetchClientes();
    }, []);

    return (
        <div>
            <h1>Gestão de Clientes</h1>

            <table border={1} style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>CPF</th>
                    <th>Email</th>
                    <th>Telefone</th>
                </tr>
                </thead>
                <tbody>
                {/* Mapeia a lista de clientes e cria uma linha para cada um */}
                {clientes.map(cliente => (
                    <tr key={cliente.cpf}>
                        <td>{cliente.nome}</td>
                        <td>{cliente.cpf}</td>
                        <td>{cliente.email}</td>
                        <td>{cliente.telefone}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default PaginaClientes;