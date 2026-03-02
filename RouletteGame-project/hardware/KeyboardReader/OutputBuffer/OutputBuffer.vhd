library ieee;
use ieee.std_logic_1164.all;

entity OutputBuffer is
	port(
	D : in std_logic_vector(3 downto 0);
	Load : in std_logic;
	ACK : in std_logic;
	RESET : in std_logic;
	CLK : in std_logic;
	OBfree : out std_logic;
	Dval : out std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end OutputBuffer;
	
architecture structural of OutputBuffer is

component BufferControl is 
	port( 
	Load : in std_logic;
	ACK : in std_logic;
   RESET : in std_logic;
   CLK : in std_logic;
   Wreg : out std_logic;
	OBfree : out std_logic;
	Dval : out std_logic
	);
end component;

component OutputRegister is
	port( 
	CLK : in std_logic;
	RESET : in std_logic;
	D : in std_logic_vector(3 downto 0);
	EN : in std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end component;

signal Wreg_Output : std_logic;

begin
U0 : BufferControl port map( Load => Load, ACK => ACK, RESET => RESET, CLK => CLK,
Wreg => Wreg_Output, OBfree => OBfree, Dval => Dval );

U1 : OutputRegister port map( CLK => Wreg_Output, RESET => RESET, D => D, EN => '1',
Q => Q);

end structural;