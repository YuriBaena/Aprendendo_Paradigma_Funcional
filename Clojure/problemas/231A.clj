;; link: https://codeforces.com/problemset/problem/231/A

;; Problema: Recebemos um número (x),
;;  depois recebem x linhas, das quais cada possui 3 números
;;  queremos retornar a quantidade de linhas
;;  com dois ou mais '1's


;; função
;; retorna 1 -> se tiver dois ou mais '1's
;; retorna 0 -> se tiver menos de dois '1's
(defn conta-linha
	[numeros]
	(if (< (reduce + numeros) 2)
		0
		1
	)
)

;; função reduce faz uma transformação de diversos dados para um unico

(require '[clojure.string :as str]) ; Precisa dessa biblioteca para separar cada número na linha

;; função para ler uma linha e retornar a sequencia de números
(defn parse-linha []
	(map #(Integer/parseInt %) 
    	(str/split (read-line) #" ")
    )
)

;; função para ler a qnt de linhas e contar quantas linhas tem soma maior ou igual a 2
(defn resolver []
  (let [x (Integer/parseInt (read-line))
        linhas (doall (repeatedly x parse-linha))
        ;; Aqui você usa sua função conta-linha em cada linha
        resultados (map conta-linha linhas)]
    
    ;; Soma todos os 1s para ter o total de linhas com 
    (println (reduce + resultados))
  )
)

(resolver)