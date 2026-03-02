library ieee;
use ieee.std_logic_1164.all;

entity counterPC is 
	port (
	CE, CLK, clr: in std_logic; 
	Q: out std_logic_vector (3 downto 0) 
	);
end counterPC;

architecture structural of counterPC is 

component adderPC is 
	port(
   A, B: in std_logic_vector(3 downto 0);
   Ci: in std_logic;
   Co: out std_logic;
   S: out std_logic_vector(3 downto 0)
   );
end component;

component regPC is
	port( 
	CLK : in std_logic;
	RESET : in std_logic;
	D : in std_logic_vector(3 downto 0);
	EN : in std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end component;

signal Adder_Reg: std_logic_vector (3 downto 0);
signal Reg_exit: std_logic_vector (3 downto 0);

begin

U0: adderPC port map (A => Reg_exit, B => "0001", Ci => '0', Co => open, S => Adder_Reg);
U1: regPC port map (CLK => CLK, RESET => clr, D => Adder_Reg, EN => CE, Q => Reg_exit);
Q <= Reg_exit;

end structural;
