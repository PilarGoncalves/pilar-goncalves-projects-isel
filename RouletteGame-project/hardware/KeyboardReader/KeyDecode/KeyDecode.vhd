library ieee;
use ieee.std_logic_1164.all;

entity KeyDecode is 
	port( 
	Kack : in std_logic;
	Kval : out std_logic;
	RESET : in std_logic;
	CLK : in std_logic;
	K : out std_logic_vector (3 downto 0);
	KeyPadL : in std_logic_vector (3 downto 0);
	KeyPadC : out std_logic_vector (3 downto 0)
	);
end KeyDecode; 	
	
architecture structural of KeyDecode is

component KeyScan is	
	port( 
	Kscan : in std_logic;
	RESET : in std_logic;
	CLK : in std_logic; 
	inMUX : in std_logic_vector (3 downto 0); 
	K : out std_logic_vector (3 downto 0);
	Kpress : out std_logic;
	outDEC : out std_logic_vector(3 downto 0) 
	);
end component;

component KeyControl is 
	port( 
	Kack : in std_logic;
	Kpress : in std_logic;
   RESET : in std_logic;
   CLK : in std_logic;
   Kval : out std_logic;
	Kscan : out std_logic
	);
end component;

component CLKDIV is
	generic(div: natural := 500000);
	port ( 
	clk_in: in std_logic;
	clk_out: out std_logic
	);
end component;

signal Kexit : std_logic_vector(3 downto 0);
signal Kpress_Kpress : std_logic; 
signal Kscan_Kscan : std_logic; 
signal Sclk : std_logic; 
signal notCLK : std_logic;

begin
U0: KeyScan port map ( Kscan => Kscan_Kscan, CLK => Sclk, RESET => RESET, inMUX(0) => KeyPadL(0), 
inMUX(1) => KeyPadL(1), inMUX(2) => KeyPadL(2), inMUX(3) => KeyPadL(3), K => Kexit, Kpress => Kpress_Kpress,
outDEC(0) => KeyPadC(0), outDEC(1) => KeyPadC(1), outDEC(2) => KeyPadC(2), outDEC(3) => KeyPadC(3) );

U1: KeyControl port map( Kack => Kack, Kpress => Kpress_Kpress, Kval => Kval, Kscan => Kscan_Kscan, 
CLK => notCLK, RESET => RESET );

U2: CLKDIV generic map (250000) port map( clk_in => CLK, clk_out => Sclk );

K <= Kexit;
notCLK <= not Sclk;

end structural;



