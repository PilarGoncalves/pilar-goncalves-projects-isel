library ieee;
use ieee.std_logic_1164.all;

entity KeyboardReader is 
	port( 
	RESET : in std_logic;
	CLK : in std_logic;
	Dval : out std_logic;
	D : out std_logic_vector (3 downto 0);
	KeyPadL : in std_logic_vector (3 downto 0);
	KeyPadC : out std_logic_vector (3 downto 0);
	ACK : in std_logic
	);
end KeyboardReader; 	
	
architecture structural of KeyboardReader is

component KeyDecode is	
	port( 
	Kack : in std_logic;
	Kval : out std_logic;
	RESET : in std_logic;
	CLK : in std_logic;
	K : out std_logic_vector (3 downto 0);
	KeyPadL : in std_logic_vector (3 downto 0);
	KeyPadC : out std_logic_vector (3 downto 0)
	);
end component;

component RingBuffer is
	port(
	D : in std_logic_vector(3 downto 0);
	DAV : in std_logic;
	CTS : in std_logic;
	RESET : in std_logic;
	CLK : in std_logic;
	Wreg : out std_logic;
	DAC : out std_logic;
	Q : out std_logic_vector(3 downto 0)
	);
end component;

component OutputBuffer is
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
end component;

signal Kval_DAV : std_logic;
signal K_D : std_logic_vector(3 downto 0);
signal Q_D : std_logic_vector(3 downto 0);
signal Wreg_Load : std_logic;
signal OBfree_CTS : std_logic;
signal DAC_Kack : std_logic;
signal Dval_exit : std_logic;
signal Dexit : std_logic_vector(3 downto 0);

begin
U0: KeyDecode port map ( 
	CLK => CLK, 
	RESET => RESET, 
	Kval => Kval_DAV, 
	K => K_D, 
	Kack => DAC_Kack, 
	KeyPadL(0) => KeyPadL(0),  
	KeyPadL(1) => KeyPadL(1), 
	KeyPadL(2) => KeyPadL(2), 
	KeyPadL(3) => KeyPadL(3), 
	KeyPadC(0) => KeyPadC(0), 
	KeyPadC(1) => KeyPadC(1), 
	KeyPadC(2) => KeyPadC(2), 
	KeyPadC(3) => KeyPadC(3) 
	);

U1: RingBuffer port map (
	D => K_D,
	DAV => Kval_DAV,
	CTS => OBfree_CTS,
	RESET => RESET,
	CLK => CLK,
	Wreg => Wreg_Load,
	DAC => DAC_Kack,
	Q => Q_D
	);

U2: OutputBuffer port map (
	D => Q_D,
	Load => Wreg_Load,
	ACK => ACK,
	RESET => RESET,
	CLK => CLK,
	OBfree => OBfree_CTS,
	Dval => Dval_exit,
	Q => Dexit
	);

D <= Dexit;
Dval <= Dval_exit;

end structural;



