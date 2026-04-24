;; Problema: https://codeforces.com/problemset/problem/617/A
;; Objetivo: Chegar em X usando o menor número de passos (1 a 5).

;; --- A MACRO ---
(defmacro enquanto-maior
  [v-ref passo]
  `(let [contador-local# (atom 0)]
     (while (>= @~v-ref ~passo)
       (swap! ~v-ref - ~passo) ; (v-ref = v-ref - passos) ou (distancia -= passo)
       (swap! contador-local# inc)) ; (contador-local = contador-local + 1) ou (contador++)
     @contador-local#)) ;; Retorna apenas o que contou NESTA execução

;; OBS1: o # é para criar um nome exclusivo que nao afete no programa principal
;; OBS2: o ~ é para avaliar uma expressão dentro do template da macro, injetando o valor real no lugar do símbolo
;; OBS3: o ` é para não executar no momento que ler

;; --- RESOLUÇÃO ---

(defn resolver []
  (let [x (atom (Integer/parseInt (read-line)))
        passos (atom 0)]
    
    ;; Usando a macro para "limpar" a lógica de repetição
    ;; Ela subtrai 5 enquanto puder, depois 4, e assim por diante.
    (enquanto-maior x 5 passos)
    (enquanto-maior x 4 passos)
    (enquanto-maior x 3 passos)
    (enquanto-maior x 2 passos)
    (enquanto-maior x 1 passos)

    (println @passos))
)

(resolver)