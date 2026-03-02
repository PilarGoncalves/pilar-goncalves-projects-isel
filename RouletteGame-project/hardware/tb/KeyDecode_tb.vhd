library ieee;
use ieee.std_logic_1164.all;

entity KeyDecode_tb is
end KeyDecode_tb;

architecture behavioral of KeyDecode_tb is

component KeyDecode is 
	port( 
	Kack : in std_logic;
	Kval : out std_logic;
	RESET : in std_logic;
	CLK : in std_logic;
	Kout : out std_logic_vector (3 downto 0);
	KeyPadL : in std_logic_vector (3 downto 0);
	KeyPadC : out std_logic_vector (3 downto 0)
	);
end component; 

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal Kack_tb : std_logic := '0';
signal Kval_tb : std_logic;
signal RESET_tb : std_logic := '0';
signal CLK_tb : std_logic := '0';
signal Kout_tb : std_logic_vector (3 downto 0);
signal KeyPadL_tb : std_logic_vector (3 downto 0) := "0000";
signal KeyPadC_tb : std_logic_vector (3 downto 0);

begin

UUT: KeyDecode 
		port map(Kack => Kack_tb,
			 Kval => Kval_tb,
			 RESET => RESET_tb,
			 CLK => CLK_tb,
			 Kout => Kout_tb,
			 KeyPadL => KeyPadL_tb,
			 KeyPadC => KeyPadC_tb);
			 
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
	wait for MCLK_PERIOD;
	RESET_tb <= '0'; 
        
	-- Simula ativação da linha 0 (tecla na primeira linha)
	KeyPadL_tb <= "0001";
	wait for MCLK_PERIOD;
   
   -- Simula ativação da linha 1	
	KeyPadL_tb <= "0010";
	wait for MCLK_PERIOD;
        
	-- Simula ativação da linha 2
	KeyPadL_tb <= "0100";
	wait for MCLK_PERIOD;
   
	-- Simula ativação da linha 3	
	KeyPadL_tb <= "1000";
	wait for MCLK_PERIOD;

	-- Sinaliza que a tecla foi reconhecida
	Kack_tb <= '1';
	wait for MCLK_PERIOD;
	Kack_tb <= '0';
	wait for MCLK_PERIOD;
        
	-- Simula situação sem tecla pressionada (nenhuma linha ativa)
	KeyPadL_tb <= "0000";
	wait for MCLK_PERIOD;
        
	-- Simula um estado inválido (mais do que uma linha ativa)
	KeyPadL_tb <= "1010";
	wait for MCLK_PERIOD;
        
	wait for MCLK_PERIOD*5;

	wait;

end process;
end behavioral;







