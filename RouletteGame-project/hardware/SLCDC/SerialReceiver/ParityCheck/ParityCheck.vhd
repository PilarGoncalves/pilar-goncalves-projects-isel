library ieee;
use ieee.std_logic_1164.all;

entity ParityCheck is 
	port (
	data: in std_logic;
	init: in std_logic;
	CLK: in std_logic;
	err: out std_logic
	);
end ParityCheck;

architecture structural of ParityCheck is

component counterPC is 
	port (
	CE, CLK, clr: in std_logic; 
	Q: out std_logic_vector (3 downto 0) 
	);
end component;

signal q: std_logic_vector(3 downto 0);

begin 

U1: counterPC port map (CE => data ,CLK => CLK, clr => init, Q => q);

err <= not q(0); 

end structural;
