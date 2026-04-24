# Macros em Clojure: Um Guia Prático

Macros são uma das ferramentas mais poderosas de Clojure. Enquanto funções operam sobre **valores**, macros operam sobre o **código** antes dele ser compilado. Elas permitem que você estenda a sintaxe da linguagem, criando novas construções que não seriam possíveis apenas com funções.

## 1. O Conceito de "Código como Dados" (Homoiconicidade)

Em Clojure, o código é escrito usando as mesmas estruturas de dados da linguagem (listas). Uma macro recebe essas listas, manipula-as e retorna uma nova lista que será executada.

## 2. Definindo Macros com `defmacro`

A estrutura básica é similar ao `defn`, mas o objetivo é retornar uma lista que represente um código válido.

```clojure
(defmacro ignore-se
  "Executa o corpo apenas se a condição for falsa."
  [condicao & corpo]
  `(if (not ~condicao)
     (do ~@corpo)))

;; Uso:
(ignore-se (= 1 2) 
  (println "Isso será impresso!")
  (println "Pois 1 não é igual a 2."))
```

## 3. Ferramentas Essenciais: Quote e Unquote

Para criar macros, usamos caracteres especiais que controlam quando o código deve ser tratado como "texto" (dados) ou "executado".

- **Syntax Quote (`` ` ``)**: Transforma um bloco de código em uma lista literal, garantindo que os nomes das funções sejam resolvidos corretamente (namespace-qualified).
- **Unquote (`~`)**: "Fura" o syntax quote para avaliar uma expressão e inserir seu valor.
- **Unquote Splitting (`~@`)**: Pega uma lista e "despeja" seus elementos dentro da lista pai.

```clojure
(defmacro mostrar-operacao [op x y]
  `(println "Calculando:" '~op "de" ~x "e" ~y "resultado:" (~op ~x ~y)))

(mostrar-operacao + 10 20)
;; Imprime: Calculando: + de 10 e 20 resultado: 30
```

## 4. Gerando Nomes Únicos com `auto-gensym`

Dentro de uma macro, para evitar conflitos de nomes de variáveis (conhecido como "capture de símbolos"), usamos o sufixo `#` para gerar nomes únicos.

```clojure
(defmacro com-tempo [expressao]
  `(let [inicio# (System/currentTimeMillis)
         resultado# ~expressao]
     (println "Tempo gasto:" (- (System/currentTimeMillis) inicio#) "ms")
     resultado#))

(com-tempo (reduce + (range 1000000)))
```

## 5. Macroexpand: Depurando seu Código

Como macros geram código, às vezes é difícil entender o que está acontecendo. Use `macroexpand` ou `macroexpand-1` para ver o código resultante antes da execução.

```clojure
(macroexpand-1 '(ignore-se true (println "Oi")))
;; => (if (clojure.core/not true) (do (println "Oi")))
```

## 6. Quando usar Macros? (A Regra de Ouro)

**Só use macros quando funções não forem suficientes.** As macros são ideais para:
- Mudar a ordem de avaliação (ex: decidir não executar um bloco de código).
- Criar sintaxes novas (ex: o operador `->` threading macro).
- Definir constantes ou estruturas complexas em tempo de compilação.

```clojure
;; Exemplo de Threading Macro (->) que facilita a leitura:
(-> [1 2 3]
    (conj 4)
    (rest)
    (reverse)) 
;; Equivalente a: (reverse (rest (conj [1 2 3] 4)))
```