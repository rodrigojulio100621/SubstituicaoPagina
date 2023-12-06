
package substituicaopagina;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rodri
 */
public class SubstituicaoPagina {

///OBSERVAÇÕES os algorimos so funcionam se forem executados 1 de cada vez

    public static void main(String[] args) {
        int[][] matrizRAM = new int[10][6];
        int[][] matrizSWAP = new int[100][6];

        preencherMatrizSwap(matrizSWAP);
        preencherMatrizRAM(matrizRAM, matrizSWAP);

        imprimirMatriz("Matriz RAM INICIO:", matrizRAM);
        imprimirMatriz("Matriz SWAP INICIO:", matrizSWAP);

        for (int i = 0; i < 1000; i++) {
            int instrucao = (int) (Math.random() * 100) + 1;
            executarInstrucao(instrucao, matrizRAM, matrizSWAP);

            if (i % 10 == 0) {
                zerarbitR(matrizRAM);
            }
        }

        imprimirMatriz("Matriz RAM FINAL:", matrizRAM);
        imprimirMatriz("Matriz SWAP FINAL:", matrizSWAP);
    }

    private static void preencherMatrizSwap(int[][] matrizSWAP) {
        for (int i = 0; i < matrizSWAP.length; i++) {
            matrizSWAP[i][0] = i;
            matrizSWAP[i][1] = i + 1;
            matrizSWAP[i][2] = (int) (Math.random() * 50) + 1;
            matrizSWAP[i][3] = 0;
            matrizSWAP[i][4] = 0;
            matrizSWAP[i][5] = (int) (Math.random() * 9900) + 100;
        }
    }

    private static void preencherMatrizRAM(int[][] matrizRAM, int[][] matrizSWAP) {
        for (int i = 0; i < matrizRAM.length; i++) {
            int indiceSorteado = (int) (Math.random() * 100);
            for (int j = 0; j < matrizRAM[i].length; j++) {
                matrizRAM[i][j] = matrizSWAP[indiceSorteado][j];
            }
        }
    }

    private static void executarInstrucao(int instrucao, int[][] matrizRAM, int[][] matrizSWAP) {
        boolean instrucaoEnc = false;

        for (int i = 0; i < matrizRAM.length; i++) {
            if (matrizRAM[i][1] == instrucao) {
                instrucaoEnc = true;
                matrizRAM[i][3] = 1; // setar o bit de Acesso R para 1

                if (Math.random() < 0.3) {
                    matrizRAM[i][2]++; // atualizar o dado D
                    matrizRAM[i][4] = 1; // setar o bit modificado M para 1
                }
                break;
            }
        }

        if (!instrucaoEnc) {
            
            
            ///OBSERVAÇÕES os algorimos so funcionam se forem executados 1 de cada vez
            substituicaoNRU(matrizRAM, matrizSWAP);
            //substituicaoRelogio(matrizRAM);
            //substituicaoFIFO(matrizRAM, matrizSWAP);
            //substituicaoWSCLOCK(matrizRAM, matrizSWAP);
            //substituicaoFIFOSC(matrizRAM, matrizSWAP);
            
        }

        for (int i = 0; i < matrizRAM.length; i++) {
            if (matrizRAM[i][4] == 1) {
                salvarPaginaEmSWAP(matrizRAM[i], matrizSWAP);
                matrizRAM[i][4] = 0; // resetar o Bit de modificação M para 0
            }
        }
    }
    
    
    //implementacao FIFO-SC-------------------------------------------------------------------------------

     private static void substituicaoFIFOSC(int[][] matrizRAM, int[][] matrizSWAP) {
        int indiceSubstituir = 0;

        // Procura por uma página que tenha Bit SC = 0
        for (int i = 0; i < matrizRAM.length; i++) {
            if (matrizRAM[i][5] == 0) {
                indiceSubstituir = i;
                break;
            }
        }

        // Se não encontrou uma página com Bit SC = 0, usa FIFO
        if (matrizRAM[indiceSubstituir][5] != 0) {
            indiceSubstituir = encontrarPaginaMenosRecente(matrizRAM);
        }

        int numeroPaginaSubstituir = matrizRAM[indiceSubstituir][0];

        for (int i = 0; i < matrizRAM.length; i++) {
            if (matrizRAM[i][0] == numeroPaginaSubstituir) {
                copiarPaginaDaSWAP(matrizRAM[i], matrizSWAP);
                break;
            }
        }
    }

   
    
// Implementação do algoritmo FIFO-------------------------------------------------------------------------------
    private static void substituicaoFIFO(int[][] matrizRAM, int[][] matrizSWAP) {
        // Implementação do algoritmo FIFO
        
         int indiceSubstituir = 0;

    // Encontrar a página que está na memória há mais tempo (a primeira inserida)
    for (int i = 1; i < matrizRAM.length; i++) {
        if (matrizRAM[i][5] < matrizRAM[indiceSubstituir][5]) {
            indiceSubstituir = i;
        }
    }

    // Obter o número da página a ser substituída
    int numeroPaginaSubstituir = matrizRAM[indiceSubstituir][0];

    // Procurar a página correspondente na matriz SWAP
    for (int i = 0; i < matrizSWAP.length; i++) {
        if (matrizSWAP[i][0] == numeroPaginaSubstituir) {
            // Copiar os dados da página da matriz SWAP para a RAM
            for (int j = 1; j < matrizRAM[indiceSubstituir].length; j++) {
                matrizRAM[indiceSubstituir][j] = matrizSWAP[i][j];
            }
            break;
        }
    }
    }
    
    //implemetnação substituicaoWSCLOCK-----------------------------------------------------------------------------------
    private static int ponteiroRelogio = 0;

private static void substituicaoWSCLOCK(int[][] matrizRAM, int[][] matrizSWAP) {
    int ep = (int) (Math.random() * 9900) + 100;

    while (true) {
        if (matrizRAM[ponteiroRelogio][3] == 0 && matrizRAM[ponteiroRelogio][5] > ep) {
            // Página encontrada com Bit R = 0 e EP > T, substituir
            break;
        } else {
            // Atualizar Bit R e avançar o ponteiro
            matrizRAM[ponteiroRelogio][3] = 0;
            ponteiroRelogio = (ponteiroRelogio + 1) % matrizRAM.length;
        }
    }
}

// Implementação do algoritmo NRU-------------------------------------------------------------------------------
private static void substituicaoNRU(int[][] matrizRAM, int[][] matrizSWAP) {
    List<Integer> classe0 = new ArrayList<>();
    List<Integer> classe1 = new ArrayList<>();
    List<Integer> classe2 = new ArrayList<>();
    List<Integer> classe3 = new ArrayList<>();

    // Classificar páginas nas classes
    for (int i = 0; i < matrizRAM.length; i++) {
        int r = matrizRAM[i][3];
        int m = matrizRAM[i][4];

        if (r == 0 && m == 0) {
            classe0.add(i);
        } else if (r == 0 && m == 1) {
            classe1.add(i);
        } else if (r == 1 && m == 0) {
            classe2.add(i);
        } else if (r == 1 && m == 1) {
            classe3.add(i);
        }
    }

    // Escolher uma página aleatória da classe não vazia com menor prioridade
    if (!classe0.isEmpty()) {
        substituirPagina(matrizRAM, matrizSWAP, classe0.get(0));
    } else if (!classe1.isEmpty()) {
        substituirPagina(matrizRAM, matrizSWAP, classe1.get(0));
    } else if (!classe2.isEmpty()) {
        substituirPagina(matrizRAM, matrizSWAP, classe2.get(0));
    } else if (!classe3.isEmpty()) {
        substituirPagina(matrizRAM, matrizSWAP, classe3.get(0));
    }
}

//implementa RELOGIO------------------------------------------------------------------------------------------------
 private static void substituicaoRelogio(int[][] matrizRAM) {
        while (true) {
            if (matrizRAM[ponteiroRelogio][3] == 0) {
                // Página encontrada com Bit R = 0, substituir
                break;
            } else {
                // Atualizar Bit R e avançar o ponteiro
                matrizRAM[ponteiroRelogio][3] = 0;
                ponteiroRelogio = (ponteiroRelogio + 1) % matrizRAM.length;
            }
        }
    }
//------------------------------------------------------------------------------------------------------------------
 private static int encontrarPaginaMenosRecente(int[][] matrizRAM) {
        // Encontrar a página menos recentemente utilizada (usando FIFO)
        int indiceMenosRecente = 0;
        for (int i = 1; i < matrizRAM.length; i++) {
            if (matrizRAM[i][5] < matrizRAM[indiceMenosRecente][5]) {
                indiceMenosRecente = i;
            }
        }
        return indiceMenosRecente;
    }
    private static void salvarPaginaEmSWAP(int[] paginaRAM, int[][] matrizSWAP) {
        int numeroPagina = paginaRAM[0];

        for (int i = 0; i < matrizSWAP.length; i++) {
            if (matrizSWAP[i][0] == numeroPagina) {
                for (int j = 1; j < paginaRAM.length; j++) {
                    matrizSWAP[i][j] = paginaRAM[j];
                }
                break;
            }
        }
    }

     private static void copiarPaginaDaSWAP(int[] paginaRAM, int[][] matrizSWAP) {
        int numeroPagina = paginaRAM[0];
        for (int i = 0; i < matrizSWAP.length; i++) {
            if (matrizSWAP[i][0] == numeroPagina) {
                for (int j = 1; j < paginaRAM.length; j++) {
                    paginaRAM[j] = matrizSWAP[i][j];
                }
                break;
            }
        }
    }
     
private static void substituirPagina(int[][] matrizRAM, int[][] matrizSWAP, int indicePaginaRAM) {
    // Obtenha o número da página a ser substituída
    int numeroPaginaSubstituir = matrizRAM[indicePaginaRAM][0];

    // Encontre a página correspondente na matriz SWAP
    for (int i = 0; i < matrizSWAP.length; i++) {
        if (matrizSWAP[i][0] == numeroPaginaSubstituir) {
            // Copie os dados da página da matriz SWAP para a RAM
            for (int j = 1; j < matrizRAM[indicePaginaRAM].length; j++) {
                matrizRAM[indicePaginaRAM][j] = matrizSWAP[i][j];
            }
            break;
        }
    }
}
    private static void zerarbitR(int[][] matrizRAM) {
        for (int i = 0; i < matrizRAM.length; i++) {
            matrizRAM[i][3] = 0;
        }
    }

    private static void imprimirMatriz(String mensagem, int[][] matriz) {
        System.out.println(mensagem);

        for (int[] linha : matriz) {
            for (int valor : linha) {
                System.out.print(valor + "\t");
            }
            System.out.println();
        }

        System.out.println();
    }
}

 

