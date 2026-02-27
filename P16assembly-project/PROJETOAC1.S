; Ficheiro:  PROJETOAC1.S
; Descricao: Programa do projeto final de Arquitetura de Computadores
; Autores: Margarita Sysoeva, Pilar Goncalves e Cecilia Marino
; Data: 05-06-2024

; Definicao dos valores dos simbolos utilizados no programa
;
	.equ	CPSR_BIT_I, 0b010000          ; Mascara para o bit I do registo CPSR
	.equ	STACK_SIZE, 64                ; Dimensao do stack - 64 B

	; Definicoes do porto de entrada
	.equ	INPORT_ADDRESS, 0xFF80        ; Endereco do porto de entrada

	; Definicoes do porto de saida
	.equ	OUTPORT_ADDRESS, 0xFFC0       ; Endereco do porto de saida
	.equ	OUTPORT_INIT_VAL, 0           ; Valor inicial do porto de saida

	; Definicoes do circuito pTC
	.equ	PTC_ADDRESS,  0xFF40          ; Endereco do circuito pTC

	.equ	PTC_TCR, 0                    ; Deslocamento do registo TCR do pTC
	.equ	PTC_TMR, 2                    ; Deslocamento do registo TMR do pTC
	.equ	PTC_TC,  4                    ; Deslocamento do registo TC do pTC
	.equ	PTC_TIR, 6                    ; Deslocamento do registo TIR do pTC

	.equ	PTC_CMD_START, 0              ; Comando para iniciar a contagem no pTC
	.equ	PTC_CMD_STOP, 1               ; Comando para parar a contagem no pTC

	.equ	SYSCLK_FREQ, 0x63             ; Intervalo de contagem do circuito pTC que suporta a implementacao do sysclk


	; Outras definicoes      

	.equ BLINK_TIME, 5                    ; Tempo que o LED demora a piscar (0,5s)
    .equ DEFAULT_CROSSING_TIME, 100       ; Tempo da passagem dos peoes (10s)

    .equ STATE_INITIAL, 0                 ; Valor atribuido ao estado inicial
    .equ STATE_CONFIG, 1                  ; Valor atribuido ao estado de configuracao
    .equ STATE_CROSS, 2                   ; Valor atribuido ao estado de passagem dos peoes

	.equ TRAFFIC_LIGHT_R, 0x01            ; Mascara para o LED 01 do LAPI acender a vermelho
	.equ TRAFFIC_LIGHT_G, 0x02            ; Mascara para o LED 01 do LAPI acender a verde
    .equ TRAFFIC_LIGHT_A, 0x03            ; Mascara para o LED 01 do LAPI acender a amarelo
	.equ PEDESTRIAN_LIGHT_R, 0x04         ; Mascara para o LED 02 do LAPI acender a vermelho
	.equ PEDESTRIAN_LIGHT_G, 0x08         ; Mascara para o LED 02 do LAPI acender a verde
    .equ PEDESTRIAN_LIGHT_A, 0x0C         ; Mascara para o LED 02 do LAPI acender a amarelo
	.equ MODE_R, 0x10                     ; Mascara para o LED 03 do LAPI acender a vermelho
	.equ MODE_G, 0x20                     ; Mascara para o LED 03 do LAPI acender a verde     
    .equ MODE_A, 0x30                     ; Mascara para o LED 03 do LAPI acender a amarelo

	.equ TRAFFIC_LIGHT_MASK, 0x03         ; Mascara para o LED 01 do LAPI
    .equ PEDESTRIAN_LIGHT_MASK, 0x0C      ; Mascara para o LED 02 do LAPI
    .equ MODE_MASK, 0x30                  ; Mascara para o LED 03 do LAPI
    
    .equ PEDESTRIAN_BUTTON, 0x01          ; Mascara para o botao PB1 do LAPI
	.equ CONFIG_MASK, 0x10                ; Mascara para o interruptor 4 do DIP-switch SW1 da placa SDP16
	.equ TIME, 0xE0                       ; Mascara para os interruptores 5 a 7 do DIP-switch da placa SPD16

    .equ CLEAR_ALL_BITS, 0x3F             ; Mascara para todos os bits do outport correspondentes aos 3 LEDs

    .equ FLASHING_ON_ENTRY, 0             ; Valor atribuido ao estado onde acende o LED
    .equ FLASHING_ON_DO, 1                ; Valor atribuido ao estado onde é verificado se já se passaram 0,5s em que o LED estava aceso
    .equ FLASHING_OFF_DO, 2               ; Valor atribuido ao estado onde é verificado se já se passaram 0,5s em que o LED estava apagado

    .equ STATE_CROSS_E, 3                 ; Valor atribuido ao estado state_cross_entry
    .equ STATE_CROSS_D, 4                 ; Valor atribuido ao estado state_cross_delay

; Seccao: startup
; Descricao: Guarda o código de arranque do sistema
	.section startup 
	b	_start
	ldr	pc, isr_addr
_start:
	ldr	sp, stack_top_addr
    mov r0, pc
    add lr, r0, #4
	ldr	pc, main_addr
    b   .

stack_top_addr:
	.word	stack_top
main_addr:
	.word	main
isr_addr:
	.word	isr

; Secção: text
; Descrição: Guarda o código do programa
	.text 

; Rotina:    main
; Descricao: 
; Entradas:  -
; Saidas:    -
; Efeitos:   
main:
	mov	r0, #OUTPORT_INIT_VAL             ; Guarda o valor inicial do porto de saida (0)
	bl	outport_init                      ; Apaga todos os LEDs
	mov	r0, #SYSCLK_FREQ                  
	bl	ptc_init                          
	mrs	r0, cpsr                          ; Coloca o conteudo do CPSR para o registo r0
	mov	r1, #CPSR_BIT_I                   
	orr r0, r0, r1                        ; Coloca a flag I a 1
	msr	cpsr, r0
    bl inport_read                
    mov r1, #PEDESTRIAN_BUTTON
    and r1, r1, r0                        ; Verifica o estado do botao dos peoes
    ldr r2, previous_button_addr          
    str r1, [r2, #0]                      ; Guarda no endereco de memoria correspondente o estado do botao anterior
    mov r5, #0                            ; Carrega o registo r5 a 0 para comecar no estado inicial
    mov r6, #0                            ; Carrega o registo r6 a 0 para comecar no state_flashing_on_entry, quando for para o estado inicial
    mov r7, #3                            ; Carrega o registo r7 a 0MMMMMMM para comecar no state_flashing_on_entry_config, quando for para o estado de configuração
main_loop: 
; r5 = sm_state
    bl config                             ; Verifica se o switch config esta a 1 ou a 0
    mov r0, #STATE_CONFIG
    cmp r5, r0
    beq state_config                      ; Caso o config esteja a 1 vai para o estado de configuracao
    bl check_pedestrian_button            ; Verifica se o botao dos peoes foi precionado 
    mov r0, #STATE_CONFIG
    cmp r5, r0                            
    beq state_config                      ; Se for igual vai para o estado de configuracao
    mov r0, #STATE_CROSS
    cmp r5, r0                             
    beq state_cross                       ; Se for igual vai para o estado de passagem dos peoes
    mov r0, #STATE_CROSS_E
    cmp r6, r0
    beq state_cross_entry                ; Se for igual vai para o estado onde o é guardado o tempo de referencia 
    mov r0, #STATE_CROSS_D
    cmp r6, r0
    beq state_delay                      ; Se for igual vai para o estado onde é calculado o tempo decorrido
    mov r0, #STATE_INITIAL
    cmp r5, r0                            
    beq state_initial                     ; Se for igual vai para o estado inicial 
end_if:
    b main_loop

; Rotina: pedestrian_button
; Descrição: Verifica se houve uma transicao ascendente do botao e caso isto se verifique muda para o estado de passagem de peoes
check_pedestrian_button:
    push lr                               
    push r4                               ; Guarda os valores na pilha
    mov r1, #PEDESTRIAN_BUTTON            ; Carrega no registo r1 a mascara correspondente ao botao dos peoes
    ldr r2, previous_button_addr
    ldrb r3, [r2, #0]                     ; Copia um byte da memória para o registo r3
    and r3, r3, r1                        ; Guarda o estado anterior do botao no registo r3
    bl inport_read                        
    mov r1, #PEDESTRIAN_BUTTON            ; Carrega no registo r1 a mascara correspondente ao botao dos peoes
    and r4, r1, r0                        ; Guarda o estado atual do botao no registo r4
    mov r1, #0
    cmp r3, r1                            ; Compara o estado anterior do botão com 0
    beq button_pressed                    ; Se o botao estiver a 0, significa que estava a ser precionado, logo precisamos de ver o seu estado atual (se foi solto ou nao)
    b end                                 ; Se o botao nao estava a ser precionado, nao vai ser preciso ver se foi solto ou nao
button_pressed:
    mov r1, #1
    cmp r1, r4                            ; Compara o estado atual do botão com 1
    bne end                               ; Caso o botao continue a 0, significa que nao foi solto
    ldr r2, previous_button_addr          ; Caso tenho passado para 1, significa que houve uma transicao ascendente, logo o botao foi solto
    str r4, [r2, #0]                      ; Guarda o estado atual do botao no endereco de memoria correspondente
    ;mov r11, #1
    mov r5, #2                            ; Muda para o estado de passagem dos peos
    pop r4
    pop pc                       
end:
    ldr r2, previous_button_addr 
    str r4, [r2, #0]                      ; Guarda o estado atual do botao no endereco de memoria correspondente
    mov r5, #0                            ; Continua no estado inicial
    pop r4
    pop pc                                ; Retira os valores da pilha

previous_button_addr:
    .word previous_button_addr1
    
; Rotina: state_initial
; Descrição: Acende o LED L3 a verde, o LED L2 a vermelho e o LED L1 a amarelo intermitente 
state_initial: 
; r6 = sm_state
    ;mov r0, #CLEAR_ALL_BITS              ; Seleciona todos os bits do outport que vao ser alterados
    ;bl outport_clear_bits                ; Coloca os bits selecionados a 0, ou seja, apaga todos os leds
    mov r0, #MODE_MASK                    ; Seleciona os bits do outport correspondentes ao modo que vao ser alterados
    mov r1, #MODE_G                       ; Seleciona apenas o bit da cor pretendida (verde)
    bl outport_write_bits                 ; Coloca apenas o bit correspondente à cor a 1 e o outro a 0, ou seja, LED L3 liga a verde
    mov r0, #PEDESTRIAN_LIGHT_MASK        ; Seleciona os bits do outport correspondestes ao led dos peõs que vao ser alterados
    mov r1, #PEDESTRIAN_LIGHT_R           ; Seleciona apenas o bit da cor pretendida (vermelho)
    bl outport_write_bits                 ; Coloca apenas o bit correspondente à cor a 1 e o outro a 0, ou seja, LED L2 liga a vermelho  

    mov r0, #FLASHING_ON_ENTRY            
    cmp r6, r0                            
    beq state_flashing_on_entry           ; Se for igual vai para o estado onde o LED é acesso 
    mov r0, #FLASHING_ON_DO               
    cmp r6, r0 
    beq state_flashing_on_do              ; Se for igual vai para o estado onde é verificado se já se passaram 0,5s em que o LED estava aceso
    mov r0, #FLASHING_OFF_DO
    cmp r6, r0
    beq state_flashing_off_do             ; Se for igual vai para o estado onde é verificado se já se passaram 0,5s em que o LED estava apagado
state_flashing_on_entry:
    mov r0, #TRAFFIC_LIGHT_MASK           ; Seleciona os bits do outport correspondentes ao led do transito de veiculos que vao ser alterados
    mov r1, #TRAFFIC_LIGHT_A              ; Seleciona apenas os bits da cor pretendida (amarelo)
    bl outport_write_bits                 ; Coloca apenas os bits correspondente à cor a 1, ou seja, LED L1 liga a amarelo
    bl sysclk_get_ticks
    mov r4, r0                            ; Carrega o registo r4 com o tempo de referencia
    mov r6, #1                            ; Carrega o registo r6 a 1 para passar para o estado onde se calcula o tempo decorrido
    b end_state
state_flashing_on_do:
    bl sysclk_get_ticks 
    sub r0, r0, r4                        ; Guarda no registo r0 a subtração entre o tempo atual e o tempo de referencia
    mov r1, #BLINK_TIME
    cmp r0, r1                            ; Compara o tempo de piscar com o resultado da subtração
    bhs state_flashing_off                ; Se esse resultado for superior a 0,5s entao vai para o estado em que o LED apaga
    mov r6, #1                            ; Caso esse resultado ainda foi menor que os 0,5s entao carrega o registo r6 a 1 para voltar ao estado onde se calcula o tempo decorrido
    b end_state
state_flashing_off:
    mov r0, #TRAFFIC_LIGHT_MASK          ; Seleciona os bits do outport correspondentes ao transito de veiculos que vao ser alterados
    mov r1, #0x00                        
    bl outport_write_bits                ; Apaga todos o LED dos bits selecionados
    bl sysclk_get_ticks
    mov r4, r0                           ; Carrega o registo r4 com o tempo de referencia
    mov r6, #2                           ; Carrega o registo r6 a 2 para passar para o estado onde se calcula o tempo decorrido
    b end_state
state_flashing_off_do:
    bl sysclk_get_ticks
    sub r0, r0, r4                       ; Guarda no registo r0 a subtração entre o tempo atual e o tempo de referencia
    mov r1, #BLINK_TIME
    cmp r0, r1                           ; Compara o tempo de piscar com o resultado da subtração
    bhs state_flashing_on_entry          ; Se esse resultado for superior a 0,5s entao vai para o estado em que o LED liga novamente
    mov r6, #2                           ; Caso esse resultado ainda foi menor que os 0,5s entao carrega o registo r6 a 1 para voltar ao estado onde se calcula o tempo decorrido
    b end_state
end_state: 
    b end_if                             ; Volta a funcao main_loop

; Rotina: config
; Descrição: Verifica o valor do switch config, caso esteja a 1 passa para o estado de configuracao e se estiver a 0 passa para o estado operacao (inicial ou da passegem dos peoes)
config:
    push lr
    push r4
    bl inport_read
    mov r1, #CONFIG_MASK
    and r1, r1, r0                       ; Guarda no registo r1 o estado do switch config
    mov r4, #0
    cmp r1, r4                           ; Compara o estado do config a 0
    bzc config_check                     ; Se o resultado da comparacao nao for zero, significa que o config esta a 1
    mov r5, #0                           ; Carrega o registo r5 para ir para o estado inicial
    pop r4
    pop pc
config_check:
    mov r5, #1                           ; Carrega o registo r5 para ir para o estado de configuracao
    pop r4
    pop pc

; Rotina: state_cross
; Descrição: Acende o LED L3 a verde, o LED L2 a verde e o LED L1 a vermelho 
;            O sistema mantem-se neste estado, no minimo, durante um periodo de tempo CROSSING_TIME 
;            Este tempo é estendido por iguais periodos sempre que for detetada uma nova transicao ascendente no sinal PEDESTRIAN_BUTTON 
state_cross:
; r6 = sm_state 
    mov r0, #CLEAR_ALL_BITS              ; Seleciona todos os bits do outport que vao ser alterados
    bl outport_clear_bits                ; Coloca os bits selecionados a 0, ou seja, apaga todos os leds
    mov r0, #MODE_MASK                   ; Seleciona os bits do outport correspondentes ao modo que vao ser alterados
    mov r1, #MODE_G                      ; Seleciona apenas o bit da cor pretendida (verde)
    bl outport_write_bits                ; Coloca apenas o bit correspondente à cor a 1 e o outro a 0, ou seja, LED L3 liga a verde
    mov r0, #PEDESTRIAN_LIGHT_MASK       ; Seleciona os bits do outport correspondestes ao led dos peõs que vao ser alterados
    mov r1, #PEDESTRIAN_LIGHT_G          ; Seleciona apenas o bit da cor pretendida (verde)
    bl outport_write_bits                ; Coloca apenas o bit correspondente à cor a 1 e o outro a 0, ou seja, LED L2 liga a verde
    mov r0, #TRAFFIC_LIGHT_MASK          ; Seleciona os bits do outport correspondestes ao led do transito dos veiculos que vao ser alterados
    mov r1, #TRAFFIC_LIGHT_R             ; Seleciona apenas o bit da cor pretendida (vermelho)
    bl outport_write_bits                ; Coloca apenas o bit correspondente à cor a 1 e o outro a 0, ou seja, LED L1 liga a vermelho
    mov r6, #STATE_CROSS_E               ; Carrega o registo r6 com o valor da constante STATE_CROSS_E (3)
    mov r5, #0                           ; Carrega o registo r5 com 0 para voltar ao estado inicial

state_cross_entry:
    bl sysclk_get_ticks
    mov r4, r0                           ; Carrega o registo r4 com o tempo de referencia
    mov r6, #4                           ; Carrega o registo r6 a 4 para passar para a proxima rotina    
    b state_cross_end

state_delay:
    bl sysclk_get_ticks
    sub r0, r0, r4                       ; Calcula o tempo decorrido, fazendo a subtracao entre o tempo atual e o tempo referencia
    cmp r0, r8                           ; Compara o tempo decorrido com o tempo definido para a passagem dos peoes
    bhs state_cross_end_time             ; Caso o tempo decorrido for maior ou igual ao tempo definido entao vai para o main_loop 
    mov r6, #4                           
    b state_cross_end                    ; Caso o tempo percorrido seja inferior ao tempo definido entao volta a mesma rotina 

state_cross_end_time:
    mov r6, #1                           ; Carrega o registo r6 com 1 para nao ir para nenhum estado do state_cross
    b state_cross_end                    ; Vai para o main_loop e depois para o estado inicial

state_cross_end:
    b end_if                             ; Volta a funcao main_loop


; Rotina: state_config
; Descricao: Acende o LED L3 a amarelo, o LED L2 a verde intermitente e o LED L1 a amarelo intermitente. 
;            Define o valor do periodo de tempo CROSSING_TIME alterando a combinacao nos bits da entrada TIME
state_config: 
; r6 = sm_state

    mov r0, #MODE_MASK                   ; Seleciona os bits do outport correspondentes ao modo que vao ser alterados
    mov r1, #MODE_A                      ; Seleciona os bits da cor pretendida (amarelo)
    bl outport_set_bits                  ; Coloca os bits correspondente à cor a 1, ou seja, LED L3 liga a amarelo

    mov r0, #FLASHING_ON_ENTRY           
    cmp r6, r0
    beq state_flashing_on_entry_config   ; Se for igual vai para o estado onde o LED é acesso
    mov r0, #FLASHING_ON_DO
    cmp r6, r0 
    beq state_flashing_on_do_config      ; Se for igual vai para o estado onde é verificado se já se passaram 0,5s em que o LED estava aceso
    mov r0, #FLASHING_OFF_DO
    cmp r6, r0
    beq state_flashing_off_do_config     ; Se for igual vai para o estado onde é verificado se já se passaram 0,5s em que o LED estava apagado
 
state_flashing_on_entry_config:
    mov r0, #TRAFFIC_LIGHT_MASK          ; Seleciona os bits do outport correspondentes ao led do transito de veiculos que vao ser alterados
    mov r1, #TRAFFIC_LIGHT_A             ; Seleciona apenas os bits da cor pretendida (amarelo)
    bl outport_write_bits                ; Coloca apenas os bits correspondente à cor a 1, ou seja, LED L1 liga a amarelo
    mov r0, #PEDESTRIAN_LIGHT_MASK       ; Selelciona os bits do outport correspondentes ao led dos peoes que vao ser alterados
    mov r1, #PEDESTRIAN_LIGHT_G          ; Seleciona apenas o bit da cor pretendida (verde)
    bl outport_write_bits                ; Coloca apenas o bit correspondente à cor a 1 e o outro a 0, ou seja, LED L2 liga a verde
    bl sysclk_get_ticks
    mov r4, r0                           ; Carrega o registo r4 com o tempo de referencia
    mov r6, #1                           ; Carrega o registo r6 a 1 para passar para a proxima rotina
    b end_state_config
state_flashing_on_do_config:             
    bl sysclk_get_ticks
    sub r0, r0, r4                       ; Guarda no registo r0 a subtração entre o tempo atual e o tempo de referencia
    mov r1, #BLINK_TIME
    cmp r0, r1                           ; Compara o tempo de piscar com o resultado da subtração
    bhs state_flashing_off_config        ; Se esse resultado for superior a 0,5s entao vai para o estado em que o LED apaga
    mov r6, #1                           ; Caso esse resultado ainda foi menor que os 0,5s entao carrega o registo r6 a 1 para voltar ao estado onde se calcula o tempo decorrido
    b end_state_config
state_flashing_off_config:
    mov r0, #TRAFFIC_LIGHT_MASK          ; Seleciona os bits do outport correspondentes ao transito de veiculos que vao ser alterados
    mov r1, #0x00                        
    bl outport_write_bits                ; Apaga todos o LED dos bits selecionados
    mov r0, #PEDESTRIAN_LIGHT_MASK       ; Seleciona os bits do outport correspondentes ao transito de veiculos que vao ser alterados
    mov r1, #0x00                  
    bl outport_write_bits                ; Apaga todos o LED dos bits selecionados
    bl sysclk_get_ticks
    mov r4, r0                           ; Carrega o registo r4 com o tempo de referencia
    mov r6, #2                           ; Carrega o registo r6 a 2 para passar para o estado onde se calcula o tempo decorrido
    b end_state_config
state_flashing_off_do_config:
    bl sysclk_get_ticks                  
    sub r0, r0, r4                       ; Guarda no registo r0 a subtração entre o tempo atual e o tempo de referencia
    mov r1, #BLINK_TIME
    cmp r0, r1                           ; Compara o tempo de piscar com o resultado da subtração
    bhs state_flashing_on_entry_config   ; Se esse resultado for superior a 0,5s entao vai para o estado em que o LED acende novamente
    mov r6, #2                           ; Caso esse resultado ainda foi menor que os 0,5s entao carrega o registo r6 a 1 para voltar ao estado onde se calcula o tempo decorrido
    bl crossing_time                     ; Chama a funcao crossing_time para alterar os valores definidos nos bits correspondentes ao TIME
    b end_state_config
end_state_config: 
    b end_if                             ; Volta a funcao main_loop

; Rotina: crossing_time
; Descricao: Le os bits correspondestes ao TIME e guarda no registo r8 o valor definido para a passagem dos peoes 
crossing_time:
    push lr
    bl inport_read
    mov r1, #TIME                       ; Carrega o registo r1 com a mascara do TIME
    and r0, r0, r1                      ; Copia para o registo r0 o valor dos bits do TIME
    lsr r0, r0, #5                      ; Desloca os 3 bits do TIME 5 bits para a direita 
    add r0, r0, r0                      ; Adiciona o valor de r0 a ele mesmo, multiplicando por 2 para deslocar para a esquerda
    ldr r1, array_addr                  ; Guarda o endereco de memoria do array no registo r1 
    ldr r8, [r1 ,r0]                    ; Guarda o valor do array no endereco calculado (r1 + r0) no registo r8 
    pop pc 

array_addr:
    .word array

; Rotina:    isr
; Descricao: Incrementa o valor da variável global sysclk.
; Entradas:  -
; Saidas:    -
; Efeitos:
isr: 
    push r0
    push r1
    ldr r1, sysclk_addr
    ldr r0, [r1, #0]
    add r0, r0, #1
    str r0, [r1,#0]
    mov r0, #PTC_ADDRESS & 0xFF
    movt r0, #(PTC_ADDRESS >> 8) & 0xFF
    strb r2, [r0, #PTC_TIR]               ; Ativa o valor de TIR que é active LOW de modo a sair do pTC a saida nINT ativa que demonstra o pedido de interrupção
    pop r1
    pop r0
    movs pc, lr

; Rotina: sysclk_init
; Descricao: Inicia uma nova contagem no periferico pTC com o intervalo de
;            contagem recebido em R0, em ticks, limpando eventuais pedidos de
;            interrupcao pendentes e iniciando com o valor zero a variavel
;            global sysclk.
;            Interface exemplo: void sysclk_init( uint8_t interval );
; Entradas:  R0 - Valor do novo intervalo de contagem, em ticks.
; Saidas:    -
; Efeitos:   Inicia a contagem no periferico a partir do valor zero, limpando
;            eventuais pedidos de interrupcao pendentes e iniciando com o
;            valor zero a variavel global sysclk
sysclk_init:
	push lr 
    push r4
    push r5
    mov r4, #0 
    ldr r5, sysclk_addr
    str r4, [r5]
    mov r0, r0 
    bl ptc_init 
    mov r5, r0
    pop r5
    pop r4
    pop pc

; Rotina: sysclk_get_ticks
; Descricao: Devolve o valor corrente da variável global sysclk.
;            Interface exemplo: uint16_t sysclk_get_ticks ( );
; Entradas:  -
; Saidas:    
; Efeitos:   -
sysclk_get_ticks:
    ldr r1 , sysclk_addr
    ldrb r0, [r1, #0] 
    mov pc, lr

	sysclk_addr:
		.word sysclk

; Gestor de periférico para o porto de entrada
;

; Rotina:    inport_read
; Descricao: Adquire e devolve o valor corrente do porto de entrada.
;            Interface exemplo: uint8_t inport_read( );
; Entradas:  -
; Saidas:    R0 - valor adquirido do porto de entrada
; Efeitos:   -
inport_read:
	ldr	r1, inport_addr
	ldrb r0, [r1, #0]
	mov	pc, lr

inport_addr:
	.word	INPORT_ADDRESS

; Gestor de periférico para o porto de saída
;

; Rotina: outport_write_bits 
; Descricao: Atribui aos bits do porto de saida identificados com o valor 1 em R0 o valor dos bits correspondentes de R1
outport_write_bits:
    push lr
    ldr r2, outport_img_addr
    ldr r3, [ r2, #0]
    and r1, r0, r1
    mvn r0, r0
    and r0, r3, r0
    orr r0, r0, r1
    strb r0, [ r2, #0]
    bl outport_write
    pop pc

; Rotina:    outport_set_bits
; Descricao: Atribui o valor logico 1 aos bits do porto de saida identificados
;            com o valor 1 em R0. O valor dos outros bits nao e alterado.
;            Interface exemplo: void outport_set_bits( uint8_t pins_mask );
; Entradas:  R0 - Mascara com a especificacao do indice dos bits a alterar.
; Saidas:    -
; Efeitos:   Altera o valor da variavel global outport_img.
outport_set_bits:
	push lr
	ldr	r1, outport_img_addr
	ldrb r2, [r1, #0]
	orr	r0, r2, r0
	strb r0, [r1, #0]
	bl	outport_write
	pop	pc

; Rotina:    outport_clear_bits
; Descricao: Atribui o valor logico 0 aos bits do porto de saida identificados
;            com o valor 1 em R0. O valor dos outros bits nao e alterado.
;            Interface exemplo: void outport_clear_bits( uint8_t pins_mask );
; Entradas:  R0 - Mascara com a especificacao do indice dos bits a alterar.
; Saidas:    -
; Efeitos:   Altera o valor da variavel global outport_img.
outport_clear_bits:
	push lr
	ldr	r1, outport_img_addr
	ldrb r2, [r1, #0]
	mvn	r0, r0
	and	r0, r2, r0
	strb r0, [r1]
	bl	outport_write
	pop	pc

; Rotina:    outport_init
; Descricao: Faz a iniciacao do porto de saida, nele estabelecendo o valor
;            recebido em R0.
;            Interface exemplo: void outport_init( uint8_t value );
; Entradas:  R0 - Valor a atribuir ao porto de saida.
; Saidas:    -
; Efeitos:   Altera o valor da variavel global outport_img.
outport_init:
	push lr
	ldr	r1, outport_img_addr
	strb r0, [r1]
	bl	outport_write
	pop	pc

outport_img_addr:
	.word	outport_img

; Rotina:    outport_write
; Descricao: Escreve no porto de saida o valor recebido em R0.
;            Interface exemplo: void outport_write( uint8_t value );
; Entradas:  R0 - valor a atribuir ao porto de saida.
; Saidas:    -
; Efeitos:   -
outport_write:
	ldr	r1, outport_addr
	strb  r0, [r1, #0]
	mov	pc, lr

outport_addr:
	.word	OUTPORT_ADDRESS

; Gestor de periférico para o Pico Timer/Counter (pTC)
;

; Rotina:    ptc_init
; Descricao: Faz a iniciacao do periférico pTC, habilitando o seu funcionamento
;            em modo continuo e com o intervalo de contagem recebido em R0, em
;            ticks.
;            Interface exemplo: void ptc_init( uint8_t interval );
; Entradas:  R0 - Valor do novo intervalo de contagem, em ticks.
; Saidas:    -
; Efeitos:   Inicia a contagem no periferico a partir do valor zero, limpando
;            o pedido de interrupcao eventualmente pendente.
ptc_init:
    push lr
	ldr	r1, PTC_ADDR
	mov	r2, #PTC_CMD_STOP
	strb r2, [r1, #PTC_TCR]
	strb r0, [r1, #PTC_TMR]
    bl  ptc_clr_irq
	mov	r2, #PTC_CMD_START
	strb r2, [r1, #PTC_TCR]
	pop pc

; Rotina:    ptc_start
; Descricao: Habilita a contagem no periferico pTC.
;            Interface exemplo: void ptc_start( );
; Entradas:  -
; Saidas:    -
; Efeitos:   -
ptc_start:
	ldr	r0, PTC_ADDR
	mov	r1, #PTC_CMD_START
	strb r1, [r0, #PTC_TCR]
	mov	pc, lr

; Rotina:    ptc_stop
; Descricao: Para a contagem no periferico pTC.
;            Interface exemplo: void ptc_stop( );
; Entradas:  -
; Saidas:    -
; Efeitos:   O valor do registo TC do periferico e colocado a zero.
ptc_stop:
	ldr	r0, PTC_ADDR
	mov	r1, #PTC_CMD_STOP
	strb r1, [r0, #PTC_TCR]
	mov	pc, lr

; Rotina:    ptc_get_value
; Descricao: Devolve o valor corrente da contagem do periferico pTC.
;            Interface exemplo: uint8_t ptc_get_value( );
; Entradas:  -
; Saidas:    R0 - O valor corrente do registo TC do periferico.
; Efeitos:   -
ptc_get_value:
	ldr	r1, PTC_ADDR
	ldrb r0, [r1, #PTC_TC]
	mov	pc, lr

; Rotina:    ptc_clr_irq
; Descricao: Sinaliza o periferico pTC que foi atendido um pedido de
;            interrupção.
;            Interface exemplo: void ptc_clr_irq( );
; Entradas:  -
; Saidas:    -
; Efeitos:   -
ptc_clr_irq:
	ldr	r0, PTC_ADDR
	strb r1, [r0, #PTC_TIR]
	mov	pc, lr

PTC_ADDR:
	.word	PTC_ADDRESS

; Seccao:    data
; Descricao: Guarda as variáveis globais
	.data 
outport_img:
	.space	1

	.align
sysclk:
	.space	2

previous_button_addr1:
    .space 2

array: 
    .word 100, 200, 300, 400, 600, 600, 600, 600

; Seccao:    stack
; Descricao: Implementa a pilha com o tamanho definido pelo simbolo STACK_SIZE
	.stack 
	.space	STACK_SIZE
stack_top:
