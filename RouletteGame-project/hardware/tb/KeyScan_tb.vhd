library ieee;
use ieee.std_logic_1164.all;

entity KeyScan_tb is
end KeyScan_tb;

architecture behavioral of KeyScan_tb is

component KeyScan is 
	port( 
	Kscan : in std_logic;
	RESET : in std_logic;
	CLK : in std_logic; 
	inMUX : in std_logic_vector (3 downto 0); 
	K : out std_logic_vector (3 downto 0);
	Kpress : out std_logic;
	outDEC : out std_logic_vector(3 downto 0) 
	);
end component;

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal Kscan_tb : std_logic := '0';
signal RESET_tb : std_logic := '0';
signal CLK_tb : std_logic := '0'; 
signal inMUX_tb : std_logic_vector (3 downto 0) := "0000"; 
signal K_tb : std_logic_vector (3 downto 0);
signal Kpress_tb : std_logic;
signal outDEC_tb : std_logic_vector(3 downto 0);

begin

UUT: KeyScan
		port map(Kscan => Kscan_tb,
					RESET => RESET_tb,
					CLK => CLK_tb,
					inMUX => inMUX_tb,
					K => K_tb,
					Kpress => Kpress_tb,
					outDEC => outDEC_tb);
			
clk_gen : process 
begin 
		CLK_tb <= '0';
		wait for MCLK_HALF_PERIOD;
		CLK_tb <= '1';
		wait for MCLK_HALF_PERIOD;
end process;
					
stimulus: process
begin

	-- Reset inicial
	RESET_tb <= '1';
	Kscan_tb <= '0';
	inMUX_tb <= "0000";
	wait for MCLK_PERIOD;
	
	RESET_tb <= '0'; 
   
	-- Ativa o varrimento do teclado
	Kscan_tb <= '1';
	
	-- Simula tecla premida na coluna 0 (espera-se Kpress = '1')
	inMUX_tb <= "0001"; 
	wait for MCLK_PERIOD;
   
	-- Simula tecla premida na coluna 1 (espera-se Kpress = '1')
	inMUX_tb <= "0010"; 
	wait for MCLK_PERIOD;
   
   -- Simula tecla premida na coluna 2 (espera-se Kpress = '1')	
   inMUX_tb <= "0100"; 
   wait for MCLK_PERIOD;
        
	-- Simula tecla premida na coluna 3 (espera-se Kpress = '1')	  
   inMUX_tb <= "1000"; 
   wait for MCLK_PERIOD;
	
	-- Nenhuma tecla premida (todas as colunas a '1') → espera-se Kpress = '0'
	inMUX_tb <= "1111";
   wait for MCLK_PERIOD;
	
	-- Desativa o varrimento do teclado
	Kscan_tb <= '0';
	wait for MCLK_PERIOD*5;

	wait;

end process;
end behavioral;
