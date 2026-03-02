library ieee;
use ieee.std_logic_1164.all;

entity regPC is
	port( 
	CLK : in std_logic;
	RESET : in std_logic;
	D : in std_logic_vector(3 downto 0);
	EN : in std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end regPC;

architecture structural of regPC is

component ffdPC is
	port( 
	CLK: in std_logic;
	RESET: in std_logic;
	SET: in std_logic;
	D: in std_logic;
	EN: in std_logic;
	Q: out std_logic
	);
end component;

begin

U0: ffdPC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(0), EN => EN, Q => Q(0));
U1: ffdPC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(1), EN => EN, Q => Q(1));
U2: ffdPC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(2), EN => EN, Q => Q(2));
U3: ffdPC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(3), EN => EN, Q => Q(3));

end structural;