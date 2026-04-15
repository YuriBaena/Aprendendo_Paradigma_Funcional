# Funções em Clojure: Um Guia Prático

Clojure é uma linguagem funcional que trata as funções como "cidadãs de primeira classe". Isso significa que elas podem ser passadas como argumentos, retornadas por outras funções e armazenadas em estruturas de dados.

## 1. Definindo Funções com `defn`

A forma mais comum de definir uma função é usando a macro `defn`.

```clojure
(defn saudacao
  "Retorna uma saudação personalizada."
  [nome]
  (str "Olá, " nome "!"))

;; Chamada da função:
(saudacao "Mundo") ; => "Olá, Mundo!"
```

## 2. Funções Anônimas

Úteis para operações rápidas, como em mapeamentos ou filtros.

### Forma Curta (`#()`)
Ideal para funções muito simples. Os argumentos são referenciados por `%`, `%1`, `%2`, etc.
```clojure
(map #(+ % 10) [1 2 3]) ; => (11 12 13)
```

### Forma `fn`
Mais legível para funções anônimas complexas.
```clojure
(fn [x y] (* x y))
```

## 3. Múltiplos Arities (Sobrecarga)

Em Clojure, pode definir uma função que se comporta de formas diferentes dependendo do número de argumentos.

```clojure
(defn multiplicar
  ([] 1)
  ([x] x)
  ([x y] (* x y)))

(multiplicar)      ; => 1
(multiplicar 5)    ; => 5
(multiplicar 5 2)  ; => 10
```

## 4. Variadic Functions (Argumentos Variáveis)

Use o símbolo `&` para coletar o restante dos argumentos numa lista.

```clojure
(defn listar-nomes [primeiro & resto]
  (println "Primeiro:" primeiro)
  (println "Demais:" resto))

(listar-nomes "Ana" "Beto" "Caio")
```

## 5. Funções de Alta Ordem (Higher-Order Functions)

- **`map`**: Aplica uma função a cada item de uma coleção.
- **`filter`**: Filtra itens com base num predicado (verdadeiro/falso).
- **`reduce`**: Acumula valores num resultado único.

```clojure
(filter even? [1 2 3 4 5 6]) ; => (2 4 6)
```
