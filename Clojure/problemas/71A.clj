;; link: https://codeforces.com/problemset/problem/71/A

;; Problema: Recebo um número (x) e depois desse
;; 	recebo x palavras, das quais todas que tiverem mais
;;  de 10 letras temos que abreviar

;; abreviação: primeira_letra +
;;			   numero_de_letras_omitidas +
;;			   ultima_letra

(defn abrevia
	[palavra]
	(let [tamanho (count palavra)]
		(if (> tamanho 10)
			(str (first palavra) (- tamanho 2) (last palavra))
			palavra
		)
	)

)

;; função str concatena
;; função count retorna tamanho da palavra

(defn processar-palavras []
  (let [x (Integer/parseInt (read-line))
        palavras (doall (repeatedly x read-line))
        ;; Transforma a lista de palavras em uma lista de abreviadas
        abreviadas (map abrevia palavras)]
    
    ;; Imprime os resultados
    (doseq [resultado abreviadas]
      (println resultado)
    )
  )
)

(processar-palavras)
