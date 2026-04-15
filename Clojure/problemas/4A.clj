;; Problema: Recebo um número (x) e quero saber
;;  se é possivel transformar x = y + z, de forma
;;  que y e z sejam números inteiros positivos pares

;; x vai de 1 a 100

;; A soma de dois pares da um número par
;; Logo x tem que ser par

;; Qualquer par?
;; Não, tem que ser todos os pares exceto o 2
;; pois de 2 só entraria em 1+1 que é soma de ímpar

(defn par_maior_2
	[numero]
	(and (= 0 (rem numero 2))
		(> numero 2))
)

;; função and faz a operação lógica 'e'
;; função rem retorna resto de numero/2

;; Teste
;; Lógica para ler a entrada e imprimir o resultado
(let [input (read-line)] ; Lê a linha do teclado
(if (nil? input) ; Verifica se a entrada não está vazia
	nil
	(let [numero (Integer/parseInt input)] ; Converte string para inteiro
		(if (par_maior_2 numero)
			(println "YES")
			(println "NO"))))
)