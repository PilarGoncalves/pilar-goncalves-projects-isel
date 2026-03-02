library ieee;
use ieee.std_logic_1164.all;

entity regC_SLCDC is
	port( 
	CLK : in std_logic;
	RESET : in std_logic;
	D : in std_logic_vector(2 downto 0);
	EN : in std_logic;
	Q : out std_logic_vector(2 downto 0)
	);
end regC_SLCDC;

architecture structural of regC_SLCDC is

component ffdC is
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

U0: ffdC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(0), EN => EN, Q => Q(0));
U1: ffdC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(1), EN => EN, Q => Q(1));
U2: ffdC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(2), EN => EN, Q => Q(2));

end structural;