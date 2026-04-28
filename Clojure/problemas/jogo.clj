(ns jogo-rio.core
  (:require [clojure.set :as set])) ; Importa ferramentas de conjuntos

;; --- 1. A MACRO ---
;; Uma macro é como uma fábrica de código. Em vez de você escrever mil "ifs", 
;; ela escreve para você antes do programa começar de verdade!
(defmacro def-regras-travessia [nome-funcao & regras]
  `(defn ~nome-funcao [~'ids] ; Cria uma função com o nome que a gente escolher
     (cond ; Começa a olhar as regrinhas uma por uma
       ~@(mapcat 
           (fn [{:keys [se entao erro]}]
             ;; Para cada regra, a macro escreve o código abaixo:
             `[(and (set/subset? ~se ~'ids) ; "Se esse grupinho perigoso está aqui..."
                    ;; "...e não tem nenhum desses protetores por perto para vigiar..."
                    (not-any? (fn [x#] (contains? ~'ids x#)) ~entao))
               ~erro]) ; "...então mostra essa mensagem de erro!"
           regras)
       :else nil))) ; Se não cair em nenhuma regra, está tudo bem (nil)!

;; --- 2. CONFIGURAÇÃO DAS REGRAS ---
;; Aqui a gente usa a macro lá de cima para criar a função "validar-margem"
(def-regras-travessia validar-margem
  ;; Se o bandido tá aqui e o policial não tá... ERROU!
  {:se #{:prisioneiro} :entao #{:policial} :erro "O prisioneiro atacou a família!"}
  ;; Se o pai tá com a filha e a mãe saiu... ERROU!
  {:se #{:pai :filha1} :entao #{:mae}      :erro "O pai não pode ficar com a filha1 sem a mãe!"}
  {:se #{:pai :filha2} :entao #{:mae}      :erro "O pai não pode ficar com a filha2 sem a mãe!"}
  ;; Se a mãe tá com o filho e o pai não tá... ERROU!
  {:se #{:mae :filho1} :entao #{:pai}      :erro "A mãe não pode ficar com o filho1 sem o pai!"}
  {:se #{:mae :filho2} :entao #{:pai}      :erro "A mãe não pode ficar com o filho2 sem o pai!"})

;; --- 3. AS ENTIDADES ---
(def entidades 
  {:pai      {:id :pai      :piloto? true}  ; Pai sabe dirigir!
   :mae      {:id :mae      :piloto? true}  ; Mãe sabe dirigir!
   :policial {:id :policial :piloto? true}  ; Policial sabe dirigir!
   :prisioneiro {:id :prisioneiro :piloto? false} ; Esse aqui só vai de carona
   :filho1   {:id :filho1   :piloto? false} ; Criança não dirige...
   :filho2   {:id :filho2   :piloto? false}
   :filha1   {:id :filha1   :piloto? false}
   :filha2   {:id :filha2   :piloto? false}})

;; --- 4. ENGINE DO JOGO ---
(defn jogo []
  (println "\n============================================")
  (println "       DESAFIO DA TRAVESSIA DO RIO")
  (println "============================================")
  (println "Instruções: Digite o nome do que quer levar.")

  ;; O loop é o "coração" do jogo. Ele bate e volta até alguém ganhar ou perder.
  (loop [esquerda (set (keys entidades)) ; Todo mundo começa na esquerda
         direita  #{}                   ; Ninguém começa na direita
         lado :esquerda]                ; O barquinho começa na esquerda
    
    ;; Vamos checar se as regras
    (let [familia #{:pai :mae :filho1 :filho2 :filha1 :filha2} ; Quem é da família
          ;; Função rápida para saber se tem alguém da família na margem
          tem-familia? (fn [m] (not (empty? (set/intersection m familia))))
          
          ;; Pergunta para a macro se as margens estão em perigo
          erro-esq (validar-margem esquerda)
          erro-dir (validar-margem direita)
          
          ;; O prisioneiro  só ataca se não tiver o policial por perto
          falha-esq (if (and erro-esq (tem-familia? esquerda)) erro-esq nil)
          falha-dir (if (and erro-dir (tem-familia? direita)) erro-dir nil)
          erro (or falha-esq falha-dir)] ; Se qualquer lado deu erro, temos um problema!
      
      (cond
        ;; Se tiver erro o jogo acaba!
        erro (println "\n[!!!] FIM DE JOGO: " erro)
        
        ;; Se a direita tem 8 pessoas, todo mundo passou!
        (= (count direita) 8) (println "\n[***] VITÓRIA! Todos atravessaram!")
        
        :else
        (do
          ;; Mostra o desenho das margens para o jogador
          (println "\n" (apply str (repeat 40 "=")))
          (println " MARGEM ESQUERDA:" (vec (sort esquerda)))
          (println " MARGEM DIREITA :" (vec (sort direita)))
          (println " BARCO ESTÁ NA :" (clojure.string/upper-case (name lado)))
          (println (apply str (repeat 40 "=")))
          (print "Levar (ex: pai,filho1): ") (flush)
          
          ;; Lê o que o usuário escreveu e limpa o texto
          (let [input (clojure.string/split (read-line) #",")
                ;; Transforma o texto em símbolos (:pai, :mae...)
                escolhidos (keep #(let [k (keyword (clojure.string/trim %))] 
                                    (if (contains? entidades k) k nil)) input)
                margem-origem (if (= lado :esquerda) esquerda direita)]
            
            ;; Agora vamos ver se o jogador não fez bobagem:
            (if (and (seq escolhidos)               ; Escolheu alguém
                     (<= (count escolhidos) 2)      ; No máximo dois no barquinho!
                     (every? #(contains? margem-origem %) escolhidos) ; Eles estão aqui mesmo?
                     (some #(:piloto? (entidades %)) escolhidos))   ; Tem alguém que sabe dirigir?
              
              ;; Se tudo deu certo, movemos as pecinhas!
              (let [mover (set escolhidos)
                    ;; Tira de um lado, coloca no outro
                    n-esq (if (= lado :esquerda) (set/difference esquerda mover) (set/union esquerda mover))
                    n-dir (if (= lado :esquerda) (set/union direita mover) (set/difference direita mover))]
                ;; Reinicia o loop com as novas posições e o barco do outro lado
                (recur n-esq n-dir (if (= lado :esquerda) :direita :esquerda)))
              
              ;; Se o jogador fez coisa errada, avisa e tenta de novo
              (do (println "\n[!] INVÁLIDO: Precisa de 1 piloto, no máximo 2 pessoas e estarem na margem.")
                  (recur esquerda direita lado)))))))))

;; Começa a brincadeira!
(jogo)