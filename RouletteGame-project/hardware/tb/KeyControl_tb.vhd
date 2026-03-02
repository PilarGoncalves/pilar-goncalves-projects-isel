library ieee;
use ieee.std_logic_1164.all;

entity KeyControl_tb is
end KeyControl_tb;

architecture behavioral of KeyControl_tb is

component KeyControl is 
	port( 
	Kack : in std_logic;
	Kpress : in std_logic;
   RESET : in std_logic;
   CLK : in std_logic;
   Kval : out std_logic;
	Kscan : out std_logic
	);
end component;

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal Kack_tb : std_logic := '0';
signal Kpress_tb : std_logic := '0';
signal RESET_tb : std_logic := '0';
signal CLK_tb : std_logic := '0';
signal Kval_tb : std_logic;
signal Kscan_tb : std_logic;

begin

UUT: KeyControl
		port map(Kack => Kack_tb,
					Kpress => Kpress_tb,
					RESET => RESET_tb,
					CLK => CLK_tb,
					Kval => Kval_tb,
					Kscan => Kscan_tb);

clk_gen : process 
begin 
		CLK_tb <= '0';
		wait for MCLK_HALF_PERIOD;
		CLK_tb <= '1';
		wait for MCLK_HALF_PERIOD;
end process;
					
stimulus: process
begin

	-- Estado 00: reset ativo, sem tecla premida
   RESET_tb <= '1';
   Kack_tb <= '0';
   Kpress_tb <= '0';
   wait for MCLK_PERIOD;
   RESET_tb <= '0';

   -- Estado 01: tecla premida → ativa Kval
   Kpress_tb <= '1';
   wait for MCLK_PERIOD;

   -- Estado 10: leitura da tecla feita (Kack = 1), mas tecla ainda premida
   Kack_tb <= '1';
   wait for MCLK_PERIOD;

   -- Tecla libertada, mas ainda não sai do estado 10 porque Kack = 1
   Kpress_tb <= '0';
   wait for MCLK_PERIOD;

   -- Transita para estado 11, onde Kscan e Kval são desligados
   Kack_tb <= '0';
   wait for MCLK_PERIOD;

   -- Após 1 ciclo, regressa ao estado 0
   wait for MCLK_PERIOD;
	
   wait;

end process;
end behavioral;
