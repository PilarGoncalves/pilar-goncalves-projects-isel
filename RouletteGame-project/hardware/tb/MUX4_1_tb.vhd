library ieee;
use ieee.std_logic_1164.all;

entity MUX4_1_tb is
end MUX4_1_tb;

architecture behavioral of MUX4_1_tb is

component MUX4_1 is
	 port(
	 A, B, C, D : in std_logic;
	 S0, S1 : in std_logic;
	 Y : out std_logic
	 );
end component;

signal A_tb : std_logic;
signal B_tb : std_logic; 
signal C_tb : std_logic;
signal D_tb : std_logic;
signal S0_tb : std_logic;
signal S1_tb : std_logic;
signal Y_tb : std_logic;

begin

UUT: MUX4_1
		port map(A => A_tb,
					B => B_tb,
					C => C_tb,
					D => D_tb,
					S0 => S0_tb,
					S1 => S1_tb,
					Y => Y_tb);
					
stimulus: process
begin

	-- Teste 1: A selecionado, A='1' e espera-se Y='0'
	A_tb <= '1';
	B_tb <= '0';
	C_tb <= '0';
	D_tb <= '0';
	S0_tb <= '0';
	S1_tb <= '0';
	wait for 10 ns;

	-- Teste 2: B selecionado, B='1' e espera-se Y='0'
	A_tb <= '0';
	B_tb <= '1';
	C_tb <= '0';
	D_tb <= '0';
	S0_tb <= '1';
	S1_tb <= '0';
	wait for 10 ns;

	-- Teste 3: C selecionado, C='1' e espera-se Y='0'
	A_tb <= '0';
	B_tb <= '0';
	C_tb <= '1';
	D_tb <= '0';
	S0_tb <= '0';
	S1_tb <= '1';
	wait for 10 ns;
 
	-- Teste 4: D selecionado, D='1' e espera-se Y='0'
	A_tb <= '0';
	B_tb <= '0';
	C_tb <= '0';
	D_tb <= '1';
	S0_tb <= '1';
	S1_tb <= '1';
	wait for 10 ns;
	
	-- Teste 5: A selecionado, A='0' e espera-se Y='1'
    A_tb <= '0'; B_tb <= '0'; C_tb <= '0'; D_tb <= '0';
    S0_tb <= '0'; S1_tb <= '0';
    wait for 10 ns;

	wait;

end process;
end behavioral;
