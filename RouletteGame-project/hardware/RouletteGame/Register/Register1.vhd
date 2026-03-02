library ieee;
use ieee.std_logic_1164.all;

entity Register1 is
	port( 
	CLK : in std_logic;
	RESET : in std_logic;
	D : in std_logic_vector(3 downto 0);
	EN : in std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end Register1;

architecture structural of Register1 is

component FFD2 is
	port( 
	CLK : in std_logic;
	RESET : in std_logic;
	SET : in std_logic;
	D : in std_logic;
	EN : in std_logic;
	Q : out std_logic
	);
end component;

begin

U0: FFD2 port map(CLK => CLK, RESET => RESET, SET => '0', D => D(0), EN => EN, Q => Q(0));
U1: FFD2 port map(CLK => CLK, RESET => RESET, SET => '0', D => D(1), EN => EN, Q => Q(1));
U2: FFD2 port map(CLK => CLK, RESET => RESET, SET => '0', D => D(2), EN => EN, Q => Q(2));
U3: FFD2 port map(CLK => CLK, RESET => RESET, SET => '0', D => D(3), EN => EN, Q => Q(3));

end structural;