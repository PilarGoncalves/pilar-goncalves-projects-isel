library ieee;
use ieee.std_logic_1164.all;

entity Reg5_MAC is
	port( 
	CLK : in std_logic;
	RESET : in std_logic;
	D : in std_logic_vector(4 downto 0);
	EN : in std_logic;
	Q : out std_logic_vector(4 downto 0)
	);
end Reg5_MAC;

architecture structural of Reg5_MAC is

component FFD_MAC is
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

U0: FFD_MAC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(0), EN => EN, Q => Q(0));
U1: FFD_MAC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(1), EN => EN, Q => Q(1));
U2: FFD_MAC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(2), EN => EN, Q => Q(2));
U3: FFD_MAC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(3), EN => EN, Q => Q(3));
U4: FFD_MAC port map(CLK => CLK, RESET => RESET, SET => '0', D => D(4), EN => EN, Q => Q(4));

end structural;