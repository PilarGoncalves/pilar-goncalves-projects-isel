library ieee;
use ieee.std_logic_1164.all;

entity DEC2_4_tb is
end DEC2_4_tb;

architecture behavioral of DEC2_4_tb is

component DEC2_4 is
	 port(
	 S0, S1 : in std_logic;
	 A, B, C, D : out std_logic
	 );
end component;

signal S0_tb : std_logic;
signal S1_tb : std_logic;
signal A_tb : std_logic;
signal B_tb : std_logic; 
signal C_tb : std_logic;
signal D_tb : std_logic;

begin

UUT: DEC2_4
		port map(S0 => S0_tb,
					S1 => S1_tb,
					A => A_tb,
					B => B_tb,
					C => C_tb,
					D => D_tb
					);
					
stimulus: process
begin
	S0_tb <= '0';
	S1_tb <= '0';
	wait for 10 ns;

	S0_tb <= '1';
	S1_tb <= '0';
	wait for 10 ns;

	S0_tb <= '0';
	S1_tb <= '1';
	wait for 10 ns;

	S0_tb <= '1';
	S1_tb <= '1';
	wait for 10 ns;

	wait;

end process;
end behavioral;
