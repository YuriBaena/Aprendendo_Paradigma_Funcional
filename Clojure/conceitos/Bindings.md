# Bindings no Clojure: Guia Completo

No Clojure, "bindings" são as formas como associamos nomes (símbolos) a valores. Diferente de variáveis em linguagens imperativas, os bindings no Clojure são, por padrão, imutáveis dentro de seu escopo.

---

## 1. Global Bindings (`def`)

O `def` é usado para criar um binding global em um namespace. Uma vez definido, o símbolo refere-se ao valor em qualquer lugar daquele namespace.

```clojure
(def pi 3.14159)
;; 'pi' agora está associado ao valor 3.14159 globalmente.
```

## 2. Local Bindings (`let`)

O `let` é a forma mais comum de criar bindings locais e temporários. Ele permite definir nomes que só existem dentro do bloco de código do `let`.

```clojure
(let [x 10
      y 20]
  (+ x y)) ; Retorna 30
```

### Características do `let`:
* **Imutabilidade:** Você não "muda" o valor de `x`.
* **Sequencialidade:** Um binding pode usar o valor de um anterior.

## 3. Bindings de Função (`defn`)

Ao definir uma função, os parâmetros agem como bindings locais para o corpo da função.

```clojure
(defn saudar [nome]
  (str "Olá, " nome "!"))
```

## 4. Destructuring (Desestruturação)

Clojure permite extrair valores de coleções diretamente nos bindings.

### Em Vetores:
```clojure
(let [[primeiro segundo & resto] [1 2 3 4 5]]
  (println primeiro) ; 1
  (println resto))    ; (3 4 5)
```

### Em Mapas:
```clojure
(let [{:keys [nome idade]} {:nome "Alice" :idade 30}]
  (str nome " tem " idade " anos."))
```

## 5. Dynamic Bindings (`binding`)

Usado para valores que podem mudar dependendo do contexto de execução (thread-local), utilizando o metadado `^:dynamic`.

```clojure
(def ^:dynamic *ambiente* "Produção")

(binding [*ambiente* "Desenvolvimento"]
  (println *ambiente*)) ; "Desenvolvimento"
```

---

## Resumo

| Tipo | Escopo | Uso Principal |
| :--- | :--- | :--- |
| **def** | Global | Constantes e funções. |
| **let** | Local | Cálculos temporários. |
| **defn** | Local | Parâmetros de funções. |
| **binding** | Dinâmico | Configurações de contexto. |