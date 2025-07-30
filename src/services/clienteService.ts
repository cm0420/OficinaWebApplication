import api from './api';
import {Cliente} from "../models/Cliente.ts";

export const getClientes = async ():Promise<Cliente[]> => {
const response = await fetch(`${api}/clientes`, {});
}